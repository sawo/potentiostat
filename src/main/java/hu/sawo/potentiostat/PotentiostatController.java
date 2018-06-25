package hu.sawo.potentiostat;

import hu.sawo.potentiostat.instructions.CyclicVoltametry;
import hu.sawo.potentiostat.instructions.Instruction;
import jssc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/api")
public class PotentiostatController {

    private static final float THRESHOLD = 25;
    
    private final Logger log = LoggerFactory.getLogger(PotentiostatController.class);

    @GetMapping("/serial")
    public List<String> getSerialPorts() {
        return newArrayList(SerialPortList.getPortNames());
    }

    @GetMapping("simulation")
    public List<Float> getResults(@RequestParam("startVolt") float startVolt,
                                  @RequestParam("peakVolt") float peakVolt,
                                  @RequestParam("scanRate") int scanRate,
                                  @RequestParam("waveType") int waveType,
                                  @RequestParam("serial") String serialPortName) throws SerialPortException,
                                                                                        InterruptedException,
                                                                                        SerialPortTimeoutException,
                                                                                        IOException {
        List<Float> result = newArrayList();
        SerialPort serialPort = new SerialPort(serialPortName);
        log.debug("Opening port [{}] ...", serialPortName);
        serialPort.openPort();
        log.debug("Successful");

        String inputData =
            new CyclicVoltametry(startVolt, peakVolt, scanRate, Instruction.WaveType.values()[waveType]).toString();
        log.debug("Sending data to device: []", inputData);
        serialPort.setParams(SerialPort.BAUDRATE_9600,
                             SerialPort.DATABITS_8,
                             SerialPort.STOPBITS_1,
                             SerialPort.PARITY_NONE);
        serialPort.writeString(inputData);
        log.debug("Successful");
        
        String dataChunk = "";
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < 400; i++) {
            dataChunk = readSerialWithTimeout(serialPort);
            if (dataChunk != null) {
                buffer.append(dataChunk);
            }
        }
            
        addValuesToResultList(result, buffer.toString());
            
        System.out.println( result.size() + " results so far");
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

    private void addValuesToResultList(List<Float> resultList, String dataChunk) {
        final String[] values = dataChunk.split("\r\n");
        for (String value : values) {
            try {
                final Float floatValue = Float.valueOf(value);
                if (Math.abs(floatValue) < THRESHOLD) {
                    resultList.add(floatValue);
                }
            } catch (NumberFormatException e) {
                System.out.println("bad number: " + dataChunk);
            }
        }
    }
}
