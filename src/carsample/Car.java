package carsample;

/**
 * Carインターフェースです。
 * 
 * @author Shunichi Todoroki
 */
public interface Car {
	/**
	 * このCarがCircuit上でどのように表現されるかを表す文字列配列を返します。
	 * 
	 * @return このCarを表現する文字列配列
	 */
	public char[][] getBody();

	/**
	 * このCarのスピードを返します。
	 * 
	 * @return このCarのスピード
	 */
	public long getSpeed();
}
