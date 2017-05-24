package org.jupport.hibernate;

import java.util.List;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

public class MySQLBitwiseOrSQLFunction extends StandardSQLFunction{

	public MySQLBitwiseOrSQLFunction(String name) {
		super(name);

	}

	public MySQLBitwiseOrSQLFunction(String name, org.hibernate.type.Type typeValue) {
		super(name, typeValue);
	}

	@Override
	public String render(Type typeValue, List args, SessionFactoryImplementor factory)
	{
		 if (args.size() != 2) {
		      throw new IllegalArgumentException("MySQLBitwiseAndSQLFunction requires 2 arguments!");
		    }
		    StringBuffer buffer = new StringBuffer(args.get(0).toString());
		    buffer.append(" | ").append(args.get(1));
		    return buffer.toString();
	}	

}
