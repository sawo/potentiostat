package hu.sawo.potentiostat;

public class CV {
    private Float x;
    private Float y;

    public CV() {
    }

    public CV(String sample) {
        String[] temp = sample.split(":");
        if (temp == null || temp.length != 2) {
            throw new NumberFormatException();
        }
        x = Float.valueOf(temp[1]);
        y = Float.valueOf(temp[0]) * -1;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CV{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}
