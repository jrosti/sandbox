package com.affiini.meta;

import com.google.inject.ImplementedBy;



@ImplementedBy(DaoImpl.class)
interface Dao {
	CompositeKeyEntity create(String a, String b);

	CompositeKeyEntity findByText(String a, String b);

	Object nativeQuery();
}