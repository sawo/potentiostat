package hu.sawo.potentiostat.instructions;

public class Instruction {

    protected String encode(String instuction, Float startVolt) {
        return encode(instuction, startVolt);
    }

    protected String encode(String instruction, Integer intValue) {
        StringBuilder result = new StringBuilder();
        if (instruction != null) {
            result.append(instruction).append("!");
        } else {
            return result.toString();
        }

        if (intValue != null) {
            result.append(intValue).append("@");
        }

        return result.toString();

    }

    protected String encode(String instruction, Float startVolt, Float peakVolt, Float scanRate, WaveType waveType) {
        return encode(instruction, startVolt, peakVolt, scanRate, waveType, null);
    }
    protected String encode(String instruction, Float startVolt, Float peakVolt, Float scanRate, WaveType waveType, Integer iterations) {
        StringBuilder result = new StringBuilder();
        if (instruction != null) {
            result.append(instruction).append("!");
        } else {
            return result.toString();
        }

        if (startVolt != null) {
            result.append(startVolt).append("@");
        } else {
            return result.toString();
        }

        if (peakVolt != null) {
            result.append(peakVolt).append("#");
        } else {
            return result.toString();
        }

        if (scanRate != null) {
            result.append(scanRate).append("$");
        } else {
            return result.toString();
        }

        if (waveType != null) {
            result.append(waveType.ordinal()).append("%");
        } else {
            return result.toString();
        }

        if (iterations != null) {
            result.append(iterations).append("^");
        } else {
            return result.toString();
        }

        return result.toString();
    }

    public enum WaveType {
        CONSTANT,
        SIN_WAVE,
        TRIANGE_WAVE;
    }
}
