package carsample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 * サーキットクラスです。
 * Carインターフェースを実装したクラスのインスタンスを{@link carsample.Circuit#CIRCUIT_LANE_WIDTH}台追加することができます
 * </pre>
 * 
 * @author Shunichi Todoroki
 */
public class Circuit {
	/** サーキットレーンの幅 */
	public static final int CIRCUIT_LANE_WIDTH = 3;
	/** Carクラスに設定できる最大スピード */
	public static final long MAX_SPEED = 1000L;
	/* Carクラスが走り回れる部分を表現するサーキット場の背景文字キャラクタ */
	private static final char BACKGROUND_CHAR = '.';
	private final String circuitName;
	private final int circuitLength;
	private final int circuitWidth;
	private final int circuitLane;
	private final List<Car> cars;
	private final char[][] circuitParts;
	/* 各Carを移動させるタイミングを割り出す基準値 */
	private final Set<Long> moveInterval;

	/**
	 * コンストラクタ。
	 * 
	 * @param circuitName このサーキットオブジェクトの名前
	 * @param circuitLength このサーキットオブジェクトが表すサーキットの全長
	 * @param circuitLane このサーキットオブジェクトが表すサーキットのレーン数
	 */
	public Circuit(String circuitName, int circuitLength, int circuitLane) {
		this.circuitName = circuitName;
		this.circuitLength = circuitLength;
		this.circuitLane = circuitLane;
		this.cars = new ArrayList<>();
		this.circuitWidth = circuitLane * CIRCUIT_LANE_WIDTH;
		this.circuitParts = createCircuitParts();
		this.moveInterval = new HashSet<>();
	}

	/**
	 * <pre>
	 * Carインターフェースを実装したクラスのインスタンスを追加します。
	 * このインスタンスが保持している台数が{@link carsample.Circuit#CIRCUIT_LANE_WIDTH}の時に
	 * このメソッドを呼び出すと、引数のcarは追加されず戻り値としてfalseが返されます
	 * また、Carクラスの文字列表現である{@link carsample.Car#getBody()}の配列の大きさが、
	 * このサーキット場を表すサイズ及びレーン幅より大きい場合はIllegalArgumentExceptionがスローされます
	 * また、{@link carsample.Car#getSpeed()}は{@link carsample.Circuit#MAX_SPEED}より大きくは設定できません
	 * </pre>
	 * 
	 * @param car Carインターフェースを実装したクラスのインスタンス
	 * @return carの追加に成功した時にtrue
	 * @throws IllegalArgumentException
	 */
	public final boolean addCar(Car car) {
		if (car == null) {
			throw new NullPointerException("引数のCarオブジェクトを指定してください。");
		}
		if (cars.size() < circuitLane) {
			char[][] body = car.getBody();
			if (body == null) {
				throw new NullPointerException("このCarを表現する文字列配列をnullにすることはできません。");
			}
			int bodyWidth = body.length;
			if (bodyWidth == 0 || bodyWidth > CIRCUIT_LANE_WIDTH) {
				throw new IllegalArgumentException(
						"この車の車幅を0、あるいはサーキット場のレーン幅より大きくすることはできません。この車の車幅["
								+ bodyWidth + "], サーキット場のレーン幅["
								+ CIRCUIT_LANE_WIDTH + "]");
			}
			for (char b[] : body) {
				int bodyLength = b.length;
				if (bodyLength == 0 || bodyLength > circuitLength) {
					throw new IllegalArgumentException(
							"この車の全長を0、あるいはサーキット場の全長より大きくすることはできません。この車の全長["
									+ bodyLength + "], サーキット場の全長["
									+ circuitLength + "]");
				}
				//マルチバイトチェック？
				//for (char c : b) {
				//	String charStr = new String(new char[] { c });
				//	if (charStr.getBytes().length > 1) {
				//		throw new IllegalArgumentException(
				//				"このCarを表現する文字列配列に使用できない文字が含まれています。[" + charStr
				//						+ "]");
				//	}
				//}
			}
			if (car.getSpeed() < 1) {
				throw new IllegalArgumentException(
						"車のスピードには1以上を設定してください。現在のスピード[ " + car.getSpeed() + "]");
			}
			if (car.getSpeed() > MAX_SPEED) {
				throw new IllegalArgumentException("車のスピードが速すぎます。" + MAX_SPEED
						+ "より小さく設定してください。現在のスピード[ " + car.getSpeed() + "]");
			}
			moveInterval.add((MAX_SPEED / car.getSpeed()));
			cars.add(car);
			return true;
		}
		return false;
	}
	
	public final void start() throws Exception {
		String controlStr = createControlChars();
		System.out.println(String.format("[%s]", circuitName));
		long start = System.currentTimeMillis();
		while(true) {
			//開始から現在までのミリ秒
			long offset = System.currentTimeMillis() - start;
			boolean b = false;
			for (Long l: moveInterval) {
				if (offset % l == 0L) {
					b = true;
					break;
				}
			}
			if (!b) continue;
			char[][] copiedCircuitParts = copiedCircuitParts();
			//各車を走らせる
			int carLaneIdx = 0;
			for (Car car: cars) {
				//毎回計算しちゃう
				char[][] body = car.getBody();
				int runnnableLength = circuitLength - body[0].length;
				long mInterval = MAX_SPEED / car.getSpeed();
				int step = (int)(offset / mInterval);
				boolean even = step == 0 ? true : (step / runnnableLength) % 2 == 0;
				int position;
				if (even) {
					//左 → 右
					position = step % runnnableLength;
				} else {
					//右 → 左
					position = runnnableLength - (step % runnnableLength);
				}
				for (int idx = 0; idx<body.length; idx++) {
					char[] cs = body[idx];
					char[] circuitLane = copiedCircuitParts[carLaneIdx * CIRCUIT_LANE_WIDTH + idx + 1];
					System.arraycopy(cs, 0, circuitLane, position + 1, cs.length);
				}
				carLaneIdx++;
			}
			for (char[] parts: copiedCircuitParts) {
				System.out.println(new String(parts));
			}
			System.out.print(controlStr);
			Thread.sleep(1);
		}
	}

	/*
	 * このサーキット場の描画表現であるcharの配列を作成して返します。
	 * ┌───────────────────────┐
	 * │.......................│
	 * └───────────────────────┘
	 * というサーキット場の場合戻り値の
	 * circuitParts[0]に入るのは以下の部分
	 * ┌───────────────────────┐
	 * 要素の最初のインデックスで指定できるのが縦であることに注意
	 */
	private final char[][] createCircuitParts() {
		//+2しているのは枠線の分
		char[][] parts = new char[circuitLane * CIRCUIT_LANE_WIDTH + 2][circuitLength + 2];
		for (char[] line : parts) {
			Arrays.fill(line, BACKGROUND_CHAR);
		}
		//サーキットの枠線の四隅
		parts[0][0] = '┌';
		parts[parts.length - 1][0] = '└';
		parts[0][parts[0].length - 1] = '┐';
		parts[parts.length - 1][parts[0].length - 1] = '┘';
		//サーキットの枠線の縦線
		for (int i = 1; i < parts.length - 1; i++) {
			parts[i][0] = '│';
			parts[i][parts[0].length - 1] = '│';
		}
		//サーキットの枠線の横線
		for (int i = 1; i < parts[0].length - 1; i++) {
			parts[0][i] = '─';
			parts[parts.length - 1][i] = '─';
		}
		return parts;
	}

	private final char[][] copiedCircuitParts() {
		char[][] copiedCircuitParts = new char[circuitLane * CIRCUIT_LANE_WIDTH + 2][circuitLength + 2];
		int idx = 0;
		for (char[] carr: circuitParts) {
			System.arraycopy(carr, 0, copiedCircuitParts[idx++], 0, carr.length);
		}
		return copiedCircuitParts;
	}

	private final String createControlChars() {
		int uplineNum = circuitWidth + 2;
		byte[] uplineNumBytes = String.valueOf(uplineNum).getBytes();
		byte[] controlChar = new byte[uplineNumBytes.length + 4];
		controlChar[0] = 0x1b;
		controlChar[1] = 0x5b;
		int idx = 0;
		for (byte b: uplineNumBytes) {
			controlChar[2 + idx++] = b;
		}
		controlChar[controlChar.length - 2] = 0x46;
		controlChar[controlChar.length - 1] = 0x0d;
		return new String(controlChar);
	}
}
