public class Koordinaten
{

	private float breitengrad;
	private float laengengrad;
	
	private int nr;
	
	public Koordinaten()
	{
		breitengrad = 0;
		laengengrad = 0;
	}
	
	public Koordinaten(float alt, float lng)
	{
		this.breitengrad = alt;
		this.setLaengengrad(lng);
	}
	
	public int getNr() {
		return nr;
	}
	
	public void setNr(int nr) {
		this.nr = nr;
	}

	public float getBreitengrad() {
		return breitengrad;
	}

	public void setLatitude(float breitengrad) {
		this.breitengrad = breitengrad;
	}

	public float getLaengengrad() {
		return laengengrad;
	}

	public void setLaengengrad(float laengengrad) {
		this.laengengrad = laengengrad;
	}
	
	public Boolean isValid()
	{
		if(breitengrad < -90 || breitengrad > 90)
			return false;
		if(laengengrad <-180 || laengengrad > 180)
			return false;
		
		return true;
	}
}