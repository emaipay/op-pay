package com.hitler.core.entity.usertype;

import org.jadira.usertype.spi.shared.AbstractLongColumnMapper;
import org.joda.time.DateTime;

public class LongColumnDateTimeMapper extends AbstractLongColumnMapper<DateTime> {

	private static final long serialVersionUID = 2980060084405710106L;

	@Override
	public DateTime fromNonNullValue(Long value) {
		return new DateTime(value);
	}

	@Override
	public DateTime fromNonNullString(String s) {
		return new DateTime(s);
	}

	@Override
	public Long toNonNullValue(DateTime value) {
		return value.getMillis();
	}

	@Override
	public String toNonNullString(DateTime value) {
		return value.toString();
	}


}
