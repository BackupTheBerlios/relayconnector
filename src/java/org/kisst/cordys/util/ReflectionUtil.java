/**
Copyright 2008, 2009 Mark Hooijkaas

This file is part of the RelayConnector framework.

The RelayConnector framework is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The RelayConnector framework is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with the RelayConnector framework.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.kisst.cordys.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

	public static Constructor getConstructor(Class cls, Class[] signature) {
		Constructor[] consarr = cls.getDeclaredConstructors();
		for (int i=0; i<consarr.length; i++) {
			Class[] paramtypes = consarr[i].getParameterTypes();
			if (java.util.Arrays.equals(signature, paramtypes))
				return consarr[i];
		}
		return null;
	}
	
	public static Object invoke(Object o, String name, Object[] args) {
		return invoke(o.getClass(),o, name, args);
	}
	public static Object invoke(Class<?> c, Object o, String name, Object[] args) {
		try {
			Method m = c.getDeclaredMethod(name, getSignature(args));
			m.setAccessible(true);
			return m.invoke(o, args);
		}
		catch (NoSuchMethodException e) { throw new RuntimeException(e); }
		catch (IllegalAccessException e) { throw new RuntimeException(e); }
		catch (InvocationTargetException e) {throw new RuntimeException(e); }
	}

	public static Object invoke(Object o, Method meth, Object[] args) {
		try {
			meth.setAccessible(true);
			return meth.invoke(o, args);
		}
		catch (IllegalAccessException e) { throw new RuntimeException(e); }
		catch (InvocationTargetException e) {throw new RuntimeException(e); }
	}
	
	private static Class[] getSignature(Object[] args) {
		Class[] signature=new Class[args.length];
		for (int i=0; i<args.length; i++)
			signature[i]=args[i].getClass();
		return signature;
	}

	public static Class findClass(String name) {
		try {	
			return Class.forName(name);
		}
		catch (ClassNotFoundException e) { throw new RuntimeException(e); }
	}

	public static Object createObject(String classname, Object[] args) {
		return createObject(findClass(classname), args);
	}
	public static Object createObject(Class<?> c, Object[] args) {
		try {
			Constructor cons= c.getConstructor(getSignature(args));
			return createObject(cons,args);
		}
		catch (NoSuchMethodException e) { throw new RuntimeException(e); } 
	}
	public static Object createObject(Constructor cons, Object[] args) {
		try {
			return cons.newInstance(args);
		}
		catch (IllegalAccessException e) { throw new RuntimeException(e); } 
		catch (InstantiationException e) { throw new RuntimeException(e); }
		catch (InvocationTargetException e) { throw new RuntimeException(e); }
	}

	public static Object createObject(String classname) {
		return createObject(findClass(classname));
	}
	public static Object createObject(Class<?> c) {
		try {
			return c.newInstance();
		}
		catch (IllegalAccessException e) { throw new RuntimeException(e); } 
		catch (InstantiationException e) { throw new RuntimeException(e); }
	}
}
