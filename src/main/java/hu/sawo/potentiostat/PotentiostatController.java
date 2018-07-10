package hu.sawo.potentiostat;

import hu.sawo.potentiostat.instructions.CyclicVoltametry;
import hu.sawo.potentiostat.instructions.Instruction;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/api")
public class PotentiostatController {

    private static final float THRESHOLD = 100;

    private final Logger log = LoggerFactory.getLogger(PotentiostatController.class);

    private SerialPort serialPort;

    @PostConstruct
    public void initPotentiostat() {
        final Optional<String> hopefullyGoodPort = getSerialPorts().stream().findFirst();
        hopefullyGoodPort.ifPresent(serialPortName -> {
            serialPort = new SerialPort(serialPortName);
            try {
                log.debug("Opening port [{}] ...", serialPortName);
                serialPort.openPort();
                log.debug("Successful");
                serialPort.setParams(SerialPort.BAUDRATE_9600,
                                     SerialPort.DATABITS_8,
                                     SerialPort.STOPBITS_1,
                                     SerialPort.PARITY_NONE);
                // setting up resolution
                serialPort.writeString("resolution2@,");

            } catch (SerialPortException e) {
                log.error(e.getMessage());
                System.exit(-1);
            }

        });
    }

    @GetMapping("/serial")
    public List<String> getSerialPorts() {
        return newArrayList(SerialPortList.getPortNames());
    }

    @GetMapping("simulation")
    public List<CV> getResults(@RequestParam("startVolt") float startVolt,
                               @RequestParam("peakVolt") float peakVolt,
                               @RequestParam("scanRate") int scanRate,
                               @RequestParam("waveType") int waveType) throws SerialPortException,
                                                                              InterruptedException {
        if (serialPort == null) {
            log.error("OliView was not found");
            return newArrayList();
        }
        
        List<CV> result = newArrayList();
        // sending command
        String inputData =
            new CyclicVoltametry(startVolt, peakVolt, scanRate, Instruction.WaveType.values()[waveType]).toString();
        log.debug("Sending data to device: []", inputData);
        serialPort.writeString(inputData);
        log.debug("Successful");

        String dataChunk = "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 400; i++) {
            dataChunk = readSerialWithTimeout(serialPort);
            if (dataChunk != null) {
                buffer.append(dataChunk);
            }
        }

        addValuesToResultList(result, buffer.toString());

        System.out.println(result.size() + " results so far");
        serialPort.closePort();
        return result;
    }

    private String readSerialWithTimeout(SerialPort serialPort) throws SerialPortException, InterruptedException {
        String output = serialPort.readString();
        if (output != null) {
            return output;
        } else {
            Thread.sleep(100);
            return serialPort.readString();
        }

    }

    private void addValuesToResultList(List<CV> resultList, String dataChunk) {
        final String[] values = dataChunk.split("\r\n");
        MovingAverage ma14 = new MovingAverage(14);
        for (String value : values) {
            try {
                CV cv = new CV(value);

                // doing moving average
                ma14.add(BigDecimal.valueOf(cv.getY()));
                cv.setY(ma14.getAverage().floatValue());

                resultList.add(cv);
            } catch (NumberFormatException e) {
                System.out.println("bad number: " + dataChunk);
            }
        }
    }
}
