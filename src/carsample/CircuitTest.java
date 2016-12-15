package carsample;

public class CircuitTest {
	public static void main(String[] args) throws Exception {
		String circuitName = "鈴鹿 サーキット場";
		int circuitLength = 180;
		int circuitLane = 3;
		try {
			if (args != null) {
				if (args.length > 0) {
					circuitName = args[0];
				}
				if (args.length > 1) {
					circuitLength = Integer.parseInt(args[1]);
				}
				if (args.length > 2) {
					circuitLane = Integer.parseInt(args[2]);
				}
			}
		} catch(NumberFormatException nfe) {
            System.err.println("このプログラムの呼び出し方が間違っています。");
            System.err.println("引数とその意味は以下の通りです。");
			printUsage();
			System.exit(1);
		}
		Circuit circuit = new Circuit(circuitName, circuitLength, circuitLane);
		circuit.addCar(new Sedan());
		circuit.addCar(new Wagon());
		circuit.addCar(new ModernCar());
		circuit.start();
	}
	
	private static void printUsage() {
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println("【使用方法】");
		System.out.println("引数１：サーキット場名");
		System.out.println("引数２：サーキットの長さ(ターミナルの幅を超えた値を指定すると表示が崩れます)");
		System.out.println("引数３：サーキットレーン数(走らせられる車の数)");
		System.out.println("--------------------------------------------------------------------------------");
	}
}
