package hu.sawo.potentiostat.instructions;

public class Resolution extends Instruction {

    private Integer resolution;

    public Resolution(Integer resolution) {
        this.resolution = resolution;
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    @Override
    public String toString() {
        return encode("resolution", resolution);
    }
}
