package carsample;

public class ModernCar implements Car {
	private final long speed;
	
	public ModernCar() {
		this.speed = 20L;
	}
	
	public ModernCar(long speed) {
		this.speed = speed;
	}

	@Override
	public char[][] getBody() {
		String s1 = "   ╭┉┉╮    ";
		String s2 = "╭┉┉╯  ╰┉┉┉╮";
		String s3 = "╰┉ⓞ     ⓞ┉╯";
		char[][] body = {s1.toCharArray(), s2.toCharArray(), s3.toCharArray()};
		return body;
	}

	@Override
	public long getSpeed() {
		return speed;
	}

}
