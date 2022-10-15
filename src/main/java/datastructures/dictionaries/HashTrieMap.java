package datastructures.dictionaries;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.trie.TrieMap;
import cse332.types.BString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    public class HashTrieNode extends TrieNode<Map<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new HashMap<A, HashTrieNode>();
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            return pointers.entrySet().iterator();
        }
    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
        this.size = 0;
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) throw new IllegalArgumentException();
        if (this.root == null) this.root = new HashTrieNode();

        V returnVal;
        //no key is associated with the value meaning that the previous value is null
        if(key.isEmpty()) {
            returnVal = this.root.value;
            this.root.value = value;
        } else {
            HashTrieNode tracker = (HashTrieNode)this.root;

            for (A pointerKey: key) {
                if(!tracker.pointers.containsKey(pointerKey)) {
                    tracker.pointers.put(pointerKey, new HashTrieNode());
                }
                tracker = tracker.pointers.get(pointerKey);
            }
            returnVal = tracker.value;
            tracker.value = value;
            if (returnVal == null) this.size++;
        }
        return returnVal ;
    }

    @Override
    public V find(K key) {
        if (key == null) throw new IllegalArgumentException();
        if (this.root == null) return null;

        HashTrieNode tracker = (HashTrieNode)this.root;
        for (A pointerKey: key) {
            tracker = tracker.pointers.get(pointerKey);
            if (tracker == null) return null;
        }
        return tracker.value;
    }

    @Override
    public boolean findPrefix(K key) {
        if (key == null) throw new IllegalArgumentException();
        if (this.root == null) return false;

        HashTrieNode tracker = (HashTrieNode)this.root;
        for (A pointerKey: key) {
            tracker = tracker.pointers.get(pointerKey);
            if (tracker == null) return false;
        }
        return true;
        //V val = this.find(key);
        //return (val != null);

    }

    @Override
    public void delete(K key) {
        if (key == null) throw new IllegalArgumentException();
        delete(key.iterator(), (HashTrieNode)this.root);
    }

    private boolean delete(Iterator<A> i, HashTrieNode temp) {
        if (!i.hasNext()) {
            temp.value = null;
            return temp.pointers.isEmpty();
        }
        A keyTracker = i.next();
        boolean deleted = false;

        if (temp.pointers.containsKey(keyTracker)) {
            deleted = delete(i, temp.pointers.get(keyTracker));
        }
        if(deleted) {
            this.size--;
            temp.pointers.remove(keyTracker);
        }
        if(temp.value == null && temp.pointers.isEmpty()) {
            this.size--;
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        this.root = new HashTrieNode();
    }
}
