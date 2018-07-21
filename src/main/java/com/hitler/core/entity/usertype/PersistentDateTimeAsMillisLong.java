package com.hitler.core.entity.usertype;

import org.jadira.usertype.spi.shared.AbstractSingleColumnUserType;
import org.joda.time.DateTime;

public class PersistentDateTimeAsMillisLong extends AbstractSingleColumnUserType<DateTime, Long, LongColumnDateTimeMapper> {

	private static final long serialVersionUID = -7473647080678292504L;


}
