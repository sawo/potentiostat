package hu.sawo.potentiostat.instructions;

public class QuietTime extends Instruction {

    private Float quietTime;

    public QuietTime(Float quietTime) {
        this.quietTime = quietTime;
    }

    public String toString() {
        return encode("quietTime", quietTime);
    }
}
