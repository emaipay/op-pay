package com.hitler.core.repository;

public enum OP {
	/**
	 * EQ : 等于 = 
	 * NE : 不等于 != 
	 * LIKE : 相似 = like("%" + value + "%") 
	 * PLIKE : like("%" + value) 
	 * ALIKE : like(value + "%") 
	 * GT : 大于 > 
	 * LT : 小于 < 
	 * GTE : 大于等于 >= 
	 * LTE : 小于等于 <= IN : id in (1,2,3)
	 */
	EQ, NE, LIKE, PLIKE, ALIKE, GT, LT, GTE, LTE, IN, OR
}