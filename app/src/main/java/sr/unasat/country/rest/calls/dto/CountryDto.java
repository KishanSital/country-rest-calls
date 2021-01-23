package sr.unasat.country.rest.calls.dto;

public class CountryDto {
    private String name;
    private String capital;
    private String region;

    public CountryDto() {
    }

    public CountryDto(String name, String capital, String region) {
        this.name = name;
        this.capital = capital;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return  "Country:"+ "\n" +
                "Name= " + name + "\n" +
                "Capital= " + capital + "\n" +
                "Region= " + region + "\n" +
                "--------------------------------" + "\n";
    }
}
