package com.cscb07.taamapp.namedAlias;

import com.cscb07.taamapp.Item;
import com.cscb07.taamapp.util.Provider;

import java.util.Map;

/**
 * Provides a mapping from Lot values to corresponding Items
 */
public abstract class ItemMappingProvider extends Provider<Map<String, Item>> {
}
