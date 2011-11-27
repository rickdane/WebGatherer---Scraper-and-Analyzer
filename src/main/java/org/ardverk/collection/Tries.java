/*
 * Copyright 2005-2010 Roger Kapsi
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ardverk.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * A collection of {@link Trie} utilities
 */
public class Tries {

    /** 
     * Returns true if bitIndex is a {@link KeyAnalyzer#OUT_OF_BOUNDS_BIT_KEY}
     */
    static boolean isOutOfBoundsIndex(int bitIndex) {
        return bitIndex == KeyAnalyzer.OUT_OF_BOUNDS_BIT_KEY;
    }

    /** 
     * Returns true if bitIndex is a {@link KeyAnalyzer#EQUAL_BIT_KEY}
     */
    static boolean isEqualBitKey(int bitIndex) {
        return bitIndex == KeyAnalyzer.EQUAL_BIT_KEY;
    }

    /** 
     * Returns true if bitIndex is a {@link KeyAnalyzer#NULL_BIT_KEY} 
     */
    static boolean isNullBitKey(int bitIndex) {
        return bitIndex == KeyAnalyzer.NULL_BIT_KEY;
    }

    /** 
     * Returns true if the given bitIndex is valid. Indices 
     * are considered valid if they're between 0 and 
     * {@link Integer#MAX_VALUE}
     */
    static boolean isValidBitIndex(int bitIndex) {
        return 0 <= bitIndex && bitIndex <= Integer.MAX_VALUE;
    }

    /**
     * Returns true if both values are either null or equal
     */
    static boolean areEqual(Object a, Object b) {
        return (a == null ? b == null : a.equals(b));
    }

    /**
     * Throws a {@link NullPointerException} with the given message if 
     * the argument is null.
     */
    static <T> T notNull(T o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
        return o;
    }

    /**
     * A utility method to cast keys. It actually doesn't
     * cast anything. It's just fooling the compiler!
     */
    @SuppressWarnings("unchecked")
    static <K> K cast(Object key) {
        return (K)key;
    }
    
    private Tries() {}
    
    /**
     * Returns a synchronized instance of a {@link Trie}
     * 
     * @see Collections#synchronizedMap(Map)
     */
    public static <K, V> Trie<K, V> synchronizedTrie(Trie<K, V> trie) {
        if (trie instanceof SynchronizedTrie) {
            return trie;
        }
        
        return new SynchronizedTrie<K, V>(trie);
    }
    
    /**
     * Returns an unmodifiable instance of a {@link Trie}
     * 
     * @see Collections#unmodifiableMap(Map)
     */
    public static <K, V> Trie<K, V> unmodifiableTrie(Trie<K, V> trie) {
        if (trie instanceof UnmodifiableTrie) {
            return trie;
        }
        
        return new UnmodifiableTrie<K, V>(trie);
    }
    
    /**
     * A synchronized {@link Trie}
     */
    private static class SynchronizedTrie<K, V> implements Trie<K, V>, Serializable {
        
        private static final long serialVersionUID = 3121878833178676939L;
        
        private final Trie<K, V> delegate;
        
        public SynchronizedTrie(Trie<K, V> delegate) {
            this.delegate = Tries.notNull(delegate, "delegate");
        }


        public synchronized Entry<K, V> select(K key, 
                Cursor<? super K, ? super V> cursor) {
            return delegate.select(key, cursor);
        }


        public synchronized Entry<K, V> select(K key) {
            return delegate.select(key);
        }


        public synchronized K selectKey(K key) {
            return delegate.selectKey(key);
        }


        public synchronized V selectValue(K key) {
            return delegate.selectValue(key);
        }


        public synchronized Entry<K, V> traverse(Cursor<? super K, ? super V> cursor) {
            return delegate.traverse(cursor);
        }
        

        public synchronized Set<Entry<K, V>> entrySet() {
            return new SynchronizedSet<Entry<K, V>>(this, delegate.entrySet());
        }


        public synchronized Set<K> keySet() {
            return new SynchronizedSet<K>(this, delegate.keySet());
        }


        public synchronized Collection<V> values() {
            return new SynchronizedCollection<V>(this, 
                    delegate.values());
        }


        public synchronized void clear() {
            delegate.clear();
        }


        public synchronized boolean containsKey(Object key) {
            return delegate.containsKey(key);
        }


        public synchronized boolean containsValue(Object value) {
            return delegate.containsValue(value);
        }


        public synchronized V get(Object key) {
            return delegate.get(key);
        }


        public synchronized boolean isEmpty() {
            return delegate.isEmpty();
        }


        public synchronized V put(K key, V value) {
            return delegate.put(key, value);
        }


        public synchronized void putAll(Map<? extends K, ? extends V> m) {
            delegate.putAll(m);
        }


        public synchronized V remove(Object key) {
            return delegate.remove(key);
        }
        

        public synchronized K lastKey() {
            return delegate.lastKey();
        }


        public synchronized SortedMap<K, V> subMap(K fromKey, K toKey) {
            return new SynchronizedSortedMap<K, V>(this, 
                    delegate.subMap(fromKey, toKey));
        }


        public synchronized SortedMap<K, V> tailMap(K fromKey) {
            return new SynchronizedSortedMap<K, V>(this, 
                    delegate.tailMap(fromKey));
        }
        

        public synchronized Comparator<? super K> comparator() {
            return delegate.comparator();
        }


        public synchronized K firstKey() {
            return delegate.firstKey();
        }


        public synchronized SortedMap<K, V> headMap(K toKey) {
            return new SynchronizedSortedMap<K, V>(this, delegate.headMap(toKey));
        }


        public synchronized SortedMap<K, V> prefixMap(K prefix) {
            return new SynchronizedSortedMap<K, V>(this, 
                    delegate.prefixMap(prefix));
        }


        public synchronized int size() {
            return delegate.size();
        }
        
        @Override
        public synchronized int hashCode() {
            return delegate.hashCode();
        }
        
        @Override
        public synchronized boolean equals(Object obj) {
            return delegate.equals(obj);
        }
        
        @Override
        public synchronized String toString() {
            return delegate.toString();
        }
    }
    
    /**
     * A synchronized {@link Collection}
     */
    private static class SynchronizedCollection<E> implements Collection<E>, Serializable {
        
        private static final long serialVersionUID = 2625364158304884729L;

        private final Object lock;
        
        private final Collection<E> delegate;
        
        public SynchronizedCollection(Object lock, Collection<E> delegate) {
            this.lock = Tries.notNull(lock, "lock");
            this.delegate = Tries.notNull(delegate, "delegate");
        }


        public boolean add(E e) {
            synchronized (lock) {
                return delegate.add(e);
            }
        }


        public boolean addAll(Collection<? extends E> c) {
            synchronized (lock) {
                return delegate.addAll(c);
            }
        }


        public void clear() {
            synchronized (lock) {
                delegate.clear();
            }
        }


        public boolean contains(Object o) {
            synchronized (lock) {
                return delegate.contains(o);
            }
        }


        public boolean containsAll(Collection<?> c) {
            synchronized (lock) {
                return delegate.containsAll(c);
            }
        }


        public boolean isEmpty() {
            synchronized (lock) {
                return delegate.isEmpty();
            }
        }


        public Iterator<E> iterator() {
            synchronized (lock) {
                return delegate.iterator();
            }
        }


        public boolean remove(Object o) {
            synchronized (lock) {
                return delegate.remove(o);
            }
        }


        public boolean removeAll(Collection<?> c) {
            synchronized (lock) {
                return delegate.removeAll(c);
            }
        }


        public boolean retainAll(Collection<?> c) {
            synchronized (lock) {
                return delegate.retainAll(c);
            }
        }


        public int size() {
            synchronized (lock) {
                return delegate.size();
            }
        }


        public Object[] toArray() {
            synchronized (lock) {
                return delegate.toArray();
            }
        }


        public <T> T[] toArray(T[] a) {
            synchronized (lock) {
                return delegate.toArray(a);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (delegate) {
                return delegate.hashCode();
            }
        }
        
        @Override
        public boolean equals(Object obj) {
            synchronized (delegate) {
                return delegate.equals(obj);
            }
        }
        
        @Override
        public String toString() {
            synchronized (lock) {
                return delegate.toString();
            }
        }
    }
    
    /**
     * A synchronized {@link Set}
     */
    private static class SynchronizedSet<E> extends SynchronizedCollection<E> 
            implements Set<E> {
        
        private static final long serialVersionUID = -6998017897934241309L;

        public SynchronizedSet(Object lock, Collection<E> deleate) {
            super(lock, deleate);
        }
    }
    
    /**
     * A synchronized {@link SortedMap}
     */
    private static class SynchronizedSortedMap<K, V> implements SortedMap<K, V>, Serializable {
        
        private static final long serialVersionUID = 3654589935305688739L;

        private final Object lock;
        
        private final SortedMap<K, V> delegate;
        
        public SynchronizedSortedMap(Object lock, SortedMap<K, V> delegate) {
            this.lock = Tries.notNull(lock, "lock");
            this.delegate = Tries.notNull(delegate, "delegate");
        }


        public Comparator<? super K> comparator() {
            synchronized (lock) {
                return delegate.comparator();
            }
        }


        public Set<Entry<K, V>> entrySet() {
            synchronized (lock) {
                return new SynchronizedSet<Entry<K,V>>(lock, 
                        delegate.entrySet());
            }
        }


        public K firstKey() {
            synchronized (lock) {
                return delegate.firstKey();
            }
        }


        public SortedMap<K, V> headMap(K toKey) {
            synchronized (lock) {
                return new SynchronizedSortedMap<K, V>(lock, 
                        delegate.headMap(toKey));
            }
        }


        public Set<K> keySet() {
            synchronized (lock) {
                return new SynchronizedSet<K>(lock, delegate.keySet());
            }
        }


        public K lastKey() {
            synchronized (lock) {
                return delegate.lastKey();
            }
        }


        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            synchronized (lock) {
                return new SynchronizedSortedMap<K, V>(lock, 
                        delegate.subMap(fromKey, toKey));
            }
        }


        public SortedMap<K, V> tailMap(K fromKey) {
            synchronized (lock) {
                return new SynchronizedSortedMap<K, V>(lock, 
                        delegate.tailMap(fromKey));
            }
        }


        public Collection<V> values() {
            synchronized (lock) {
                return new SynchronizedCollection<V>(lock, delegate.values());
            }
        }


        public void clear() {
            synchronized (lock) {
                delegate.clear();
            }
        }


        public boolean containsKey(Object key) {
            synchronized (lock) {
                return delegate.containsKey(key);
            }
        }


        public boolean containsValue(Object value) {
            synchronized (lock) {
                return delegate.containsValue(value);
            }
        }


        public V get(Object key) {
            synchronized (lock) {
                return delegate.get(key);
            }
        }


        public boolean isEmpty() {
            synchronized (lock) {
                return delegate.isEmpty();
            }
        }


        public V put(K key, V value) {
            synchronized (lock) {
                return delegate.put(key, value);
            }
        }


        public void putAll(Map<? extends K, ? extends V> m) {
            synchronized (lock) {
                delegate.putAll(m);
            }
        }


        public V remove(Object key) {
            synchronized (lock) {
                return delegate.remove(key);
            }
        }


        public int size() {
            synchronized (lock) {
                return delegate.size();
            }
        }
        

        public int hashCode() {
            synchronized (delegate) {
                return delegate.hashCode();
            }
        }
        
        @Override
        public boolean equals(Object obj) {
            synchronized (delegate) {
                return delegate.equals(obj);
            }
        }
        
        @Override
        public String toString() {
            synchronized (lock) {
                return delegate.toString();
            }
        }
    }
    
    /**
     * An unmodifiable {@link Trie}
     */
    private static class UnmodifiableTrie<K, V> implements Trie<K, V>, Serializable {
        
        private static final long serialVersionUID = -7156426030315945159L;
        
        private final Trie<K, V> delegate;
        
        public UnmodifiableTrie(Trie<K, V> delegate) {
            this.delegate = Tries.notNull(delegate, "delegate");
        }


        public Entry<K, V> select(K key, final Cursor<? super K, ? super V> cursor) {
            Cursor<K, V> c = new Cursor<K, V>() {

                public Decision select(Map.Entry<? extends K, ? extends V> entry) {
                    Decision decision = cursor.select(entry);
                    switch (decision) {
                        case REMOVE:
                        case REMOVE_AND_EXIT:
                            throw new UnsupportedOperationException();
                    }
                    
                    return decision;
                }
            };
            
            return delegate.select(key, c);
        }


        public Entry<K, V> select(K key) {
            return delegate.select(key);
        }


        public K selectKey(K key) {
            return delegate.selectKey(key);
        }


        public V selectValue(K key) {
            return delegate.selectValue(key);
        }


        public Entry<K, V> traverse(final Cursor<? super K, ? super V> cursor) {
            Cursor<K, V> c = new Cursor<K, V>() {

                public Decision select(Map.Entry<? extends K, ? extends V> entry) {
                    Decision decision = cursor.select(entry);
                    switch (decision) {
                        case REMOVE:
                        case REMOVE_AND_EXIT:
                            throw new UnsupportedOperationException();
                    }
                    
                    return decision;
                }
            };
            
            return delegate.traverse(c);
        }


        public Set<Entry<K, V>> entrySet() {
            return Collections.unmodifiableSet(delegate.entrySet());
        }
        

        public Set<K> keySet() {
            return Collections.unmodifiableSet(delegate.keySet());
        }


        public Collection<V> values() {
            return Collections.unmodifiableCollection(delegate.values());
        }


        public void clear() {
            throw new UnsupportedOperationException();
        }


        public boolean containsKey(Object key) {
            return delegate.containsKey(key);
        }


        public boolean containsValue(Object value) {
            return delegate.containsValue(value);
        }


        public V get(Object key) {
            return delegate.get(key);
        }


        public boolean isEmpty() {
            return delegate.isEmpty();
        }


        public V put(K key, V value) {
            throw new UnsupportedOperationException();
        }


        public void putAll(Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }


        public V remove(Object key) {
            throw new UnsupportedOperationException();
        }


        public K firstKey() {
            return delegate.firstKey();
        }


        public SortedMap<K, V> headMap(K toKey) {
            return Collections.unmodifiableSortedMap(delegate.headMap(toKey));
        }


        public K lastKey() {
            return delegate.lastKey();
        }


        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return Collections.unmodifiableSortedMap(
                    delegate.subMap(fromKey, toKey));
        }


        public SortedMap<K, V> tailMap(K fromKey) {
            return Collections.unmodifiableSortedMap(delegate.tailMap(fromKey));
        }


        public SortedMap<K, V> prefixMap(K prefix) {
            return Collections.unmodifiableSortedMap(
                    delegate.prefixMap(prefix));
        }


        public Comparator<? super K> comparator() {
            return delegate.comparator();
        }
        

        public int size() {
            return delegate.size();
        }
        
        @Override
        public int hashCode() {
            return delegate.hashCode();
        }
        
        @Override
        public boolean equals(Object obj) {
            return delegate.equals(obj);
        }
        
        @Override
        public String toString() {
            return delegate.toString();
        }
    }
}
