package hu.sawo.potentiostat.instructions;

public class ChangeSampleRate extends Instruction {

    private Float sampleRate;

    public ChangeSampleRate(Float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public Float getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(Float sampleRate) {
        this.sampleRate = sampleRate;
    }

    @Override
    public String toString() {
        return encode("changeSampleRate", sampleRate);
    }
}
