package carsample;

public class Sedan implements Car {
	private final long speed;
	
	public Sedan() {
		this.speed = 7L;
	}
	
	public Sedan(long speed) {
		this.speed = speed;
	}

	@Override
	public char[][] getBody() {
		String s1 = "   ┌───┐  ";
		String s2 = "┌──    ──┐";
		String s3 = "└ ◉    ◉ ┘";
		char[][] body = {s1.toCharArray(), s2.toCharArray(), s3.toCharArray()};
		return body;
	}

	@Override
	public long getSpeed() {
		return speed;
	}

}
