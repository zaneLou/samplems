package org.jupport.hibernate;


import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class FineMySQL5Dialect extends MySQL5InnoDBDialect {

	 public FineMySQL5Dialect() {
	       super();
	       
	       registerFunction( "bitwise_and", new VarArgsSQLFunction( StandardBasicTypes.STRING, "", " & ", "" ) );
	       registerFunction( "bitwise_or", new VarArgsSQLFunction( StandardBasicTypes.STRING, "", " | ", "" ) );
	       //registerFunction("bitwise_and", new MySQLBitwiseAndSQLFunction("bitwise_and", StandardBasicTypes.INTEGER));
	       //registerFunction("bitwise_or", new MySQLBitwiseOrSQLFunction("bitwise_or", StandardBasicTypes.INTEGER));
	   }

	 
}
