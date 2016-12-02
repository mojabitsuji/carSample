package carsample;

public class CircuitTest {
	public static void main(String[] args) throws Exception {
		Circuit circuit = new Circuit("鈴鹿 Circuit", 180, 3);
		circuit.addCar(new Sedan());
		circuit.addCar(new Wagon());
		circuit.addCar(new ModernCar());
		circuit.start();
	}
}
