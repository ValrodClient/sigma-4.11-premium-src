package net.minecraft.optifine;

import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.NextTickListEntry;

public class NextTickHashSet extends TreeSet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private LongHashMap longHashMap = new LongHashMap();
    private int minX = Integer.MIN_VALUE;
    private int minZ = Integer.MIN_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxZ = Integer.MIN_VALUE;
    private static final int UNDEFINED = Integer.MIN_VALUE;

    public NextTickHashSet(Set oldSet) {
        Iterator it = oldSet.iterator();

        while (it.hasNext()) {
            Object obj = it.next();
            add(obj);
        }
    }

    @Override
    public boolean contains(Object obj) {
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        } else {
            NextTickListEntry entry = (NextTickListEntry) obj;
            Set set = this.getSubSet(entry, false);
            return set == null ? false : set.contains(entry);
        }
    }

    @Override
    public boolean add(Object obj) {
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        } else {
            NextTickListEntry entry = (NextTickListEntry) obj;

            Set set = this.getSubSet(entry, true);
            boolean added = set.add(entry);
            boolean addedParent = super.add(obj);

            if (added != addedParent) {
                throw new IllegalStateException("Added: " + added + ", addedParent: " + addedParent);
            } else {
                return addedParent;
            }
        }
    }

    @Override
    public boolean remove(Object obj) {
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        } else {
            NextTickListEntry entry = (NextTickListEntry) obj;
            Set set = this.getSubSet(entry, false);

            if (set == null) {
                return false;
            } else {
                boolean removed = set.remove(entry);
                boolean removedParent = super.remove(entry);

                if (removed != removedParent) {
                    throw new IllegalStateException("Added: " + removed + ", addedParent: " + removedParent);
                } else {
                    return removedParent;
                }
            }
        }
    }

    private Set getSubSet(NextTickListEntry entry, boolean autoCreate) {
        if (entry == null) {
            return null;
        } else {
            BlockPos pos = entry.field_180282_a;
            int cx = pos.getX() >> 4;
            int cz = pos.getZ() >> 4;
            return this.getSubSet(cx, cz, autoCreate);
        }
    }

    private Set getSubSet(int cx, int cz, boolean autoCreate) {
        long key = ChunkCoordIntPair.chunkXZ2Int(cx, cz);
        HashSet set = (HashSet) longHashMap.getValueByKey(key);

        if (set == null && autoCreate) {
            set = new HashSet();
            longHashMap.add(key, set);
        }

        return set;
    }

    @Override
    public Iterator iterator() {
        if (minX == Integer.MIN_VALUE) {
            return super.iterator();
        } else if (size() <= 0) {
            return Iterators.emptyIterator();
        } else {
            int cMinX = minX >> 4;
            int cMinZ = minZ >> 4;
            int cMaxX = maxX >> 4;
            int cMaxZ = maxZ >> 4;
            ArrayList listIterators = new ArrayList();

            for (int x = cMinX; x <= cMaxX; ++x) {
                for (int z = cMinZ; z <= cMaxZ; ++z) {
                    Set set = this.getSubSet(x, z, false);

                    if (set != null) {
                        listIterators.add(set.iterator());
                    }
                }
            }

            if (listIterators.size() <= 0) {
                return Iterators.emptyIterator();
            } else if (listIterators.size() == 1) {
                return (Iterator) listIterators.get(0);
            } else {
                return Iterators.concat(listIterators.iterator());
            }
        }
    }

    public void setIteratorLimits(int minX, int minZ, int maxX, int maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public void clearIteratorLimits() {
        minX = Integer.MIN_VALUE;
        minZ = Integer.MIN_VALUE;
        maxX = Integer.MIN_VALUE;
        maxZ = Integer.MIN_VALUE;
    }
}
