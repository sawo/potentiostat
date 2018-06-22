package hu.sawo.potentiostat.instructions;

public class CyclicVoltametry extends Instruction {

    private float startVolt;
    private float peakVolt;
    private float scanRate;
    private WaveType waveType;

    public CyclicVoltametry(float startVolt, float peakVolt, float scanRate, WaveType waveType) {
        this.startVolt = startVolt;
        this.peakVolt = peakVolt;
        this.scanRate = scanRate;
        this.waveType = waveType;
    }

    public float getStartVolt() {
        return startVolt;
    }

    public void setStartVolt(float startVolt) {
        this.startVolt = startVolt;
    }

    public float getPeakVolt() {
        return peakVolt;
    }

    public void setPeakVolt(float peakVolt) {
        this.peakVolt = peakVolt;
    }

    public float getScanRate() {
        return scanRate;
    }

    public void setScanRate(float scanRate) {
        this.scanRate = scanRate;
    }

    public WaveType getWaveType() {
        return waveType;
    }

    public void setWaveType(WaveType waveType) {
        this.waveType = waveType;
    }

    @Override
    public String toString() {
        return super.encode("cycVolt", startVolt, peakVolt, scanRate, waveType);
    }
}
