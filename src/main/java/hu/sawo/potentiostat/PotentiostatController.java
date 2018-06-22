package hu.sawo.potentiostat;

import jssc.SerialPortList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/api")
public class PotentiostatController {

    @GetMapping("/serial")
    public List<String> getSerialPorts() {
        return newArrayList(SerialPortList.getPortNames());
    }

    @GetMapping("simulation")
    public List<Float> getResults(@RequestParam("startVolt") float startVolt,
                                   @RequestParam("peakVolt") float peakVolt,
                                   @RequestParam("scanRate") int scanRate,
                                   @RequestParam("waveType") int waveType) {
        List<Float> result = newArrayList();
        for (int i = 0; i < 2000; i++) {
             result.add((float) (Math.random() * 5));
        }
        return result;                  

    }
}
