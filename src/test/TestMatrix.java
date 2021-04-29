package test;

import org.junit.Test;

import deeplearningGroup1.Matrix;

public class TestMatrix extends junit.framework.TestCase {
	
	private Matrix a = new Matrix(new double[][] {{1, -2}, {Math.sqrt(2), 4}});
	private Matrix b = new Matrix(new double[][] {{5, 6}, {7, -8.0045}});
	private Matrix m_plus = a.plus(b);
	private Matrix m_tran = a.transpose();
	private Matrix m_minus = a.minus(b);
	private Matrix m_mult_1 = a.multiply(b);
	private Matrix m_mult_2 = a.multiply(3);
	private Matrix m_e_mult = a.elementMultiply(b);
	private Matrix m_pow = a.pow(3);
	private Matrix m_div = a.divide(7);
	
	@Test
	public void testPlusOne() {
		assertEquals(6.0, m_plus.get(0, 0), .0001);
	}
	
	@Test
	public void testPlusTwo() {
		assertEquals(4.0, m_plus.get(0, 1), .0001);
	}
	
	@Test
	public void testPlusThree() {
		assertEquals(8.4142, m_plus.get(1, 0), .0001);
	}
	
	@Test
	public void testPlusFour() {
		assertEquals(-4.0045, m_plus.get(1, 1), .0001);
	}
	
	@Test
	public void testTransposeOne() {
		assertEquals(1.0, m_tran.get(0, 0), .0001);
	}
	
	@Test
	public void testTransposeTwo() {
		assertEquals(1.4142, m_tran.get(0, 1), .0001);
	}
	
	@Test
	public void testTransposeThree() {
		assertEquals(-2.0, m_tran.get(1, 0), .0001);
	}
	
	@Test
	public void testTransposeFour() {
		assertEquals(4.0, m_tran.get(1, 1), .0001);
	}
	
	@Test
	public void testMinusOne() {
		assertEquals(-4.0, m_minus.get(0, 0), .0001);
	}
	
	@Test
	public void testMinusTwo() {
		assertEquals(-8.0, m_minus.get(0, 1), .0001);
	}
	
	@Test
	public void testMinusThree() {
		assertEquals(-5.5858, m_minus.get(1, 0), .0001);
	}
	
	@Test
	public void testMinusFour() {
		assertEquals(12.0045, m_minus.get(1, 1), .0001);
	}
	
	@Test
	public void testEqualsOne() {
		assertTrue(a.isEqual(a));
	}
	
	@Test
	public void testEqualsTwo() {
		assertFalse(a.isEqual(b));
	}
	
	@Test
	public void testMultOne() {
		assertEquals(-9., m_mult_1.get(0, 0), .0001);
	}
	
	@Test
	public void testMultTwo() {
		assertEquals(22.009, m_mult_1.get(0, 1), .0001);
	}
	
	@Test
	public void testMultThree() {
		assertEquals(35.0711, m_mult_1.get(1, 0), 0.0001);
	}
	
	@Test
	public void testMultFour() {
		assertEquals(-23.5327, m_mult_1.get(1, 1), .0001);
	}
	
	@Test
	public void testMultFive() {
		assertEquals(3.0, m_mult_2.get(0, 0), .0001);
	}
	
	@Test
	public void testMultSix() {
		assertEquals(-6.0, m_mult_2.get(0, 1), .0001);
	}
	
	@Test
	public void testMultSeven() {
		assertEquals(4.2426, m_mult_2.get(1, 0), .0001);
	}
	
	@Test
	public void testMultEight() {
		assertEquals(12.0, m_mult_2.get(1, 1), .0001);
	}
	
	@Test
	public void testElemMultOne() {
		assertEquals(5.0, m_e_mult.get(0, 0), .0001);
	}
	
	@Test
	public void testElemMultTwo() {
		assertEquals(-12.0, m_e_mult.get(0, 1), .0001);
	}
	
	@Test
	public void testElemMultThree() {
		assertEquals(9.8995, m_e_mult.get(1, 0), .0001);
	}
	
	@Test
	public void testElemMultFour() {
		assertEquals(-32.0180, m_e_mult.get(1, 1), .0001);
	}
	
	@Test
	public void testPowOne() {
		assertEquals(1.0, m_pow.get(0, 0), .0001);
	}
	
	@Test
	public void testPowTwo() {
		assertEquals(-8.0, m_pow.get(0, 1), .0001);
	}
	
	@Test
	public void testPowThree() {
		assertEquals(2.8284, m_pow.get(1, 0), .0001);
	}
	
	@Test
	public void testPowFour() {
		assertEquals(64.0, m_pow.get(1, 1), .0001);
	}
	
	@Test
	public void testDivOne() {
		assertEquals(.1429, m_div.get(0, 0), .0001);
	}
	
	@Test
	public void testDivTwo() {
		assertEquals(-.2857, m_div.get(0, 1), .0001);
	}
	
	@Test
	public void testDivThree() {
		assertEquals(.2020, m_div.get(1, 0), .0001);
	}
	
	@Test
	public void testDivFour() {
		assertEquals(.5714, m_div.get(1, 1), .0001);
	}

}
