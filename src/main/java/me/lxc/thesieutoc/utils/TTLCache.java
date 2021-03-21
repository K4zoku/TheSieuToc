package me.lxc.thesieutoc.utils;

import java.util.function.Supplier;

public class TTLCache<V> {
  private final Supplier<V> updater;
  private final long ttl;
  private V value;
  private long lastUpdate;

  public TTLCache(Supplier<V> updater, long ttl) {
    this.updater = updater;
    this.ttl = ttl;
  }

  public void update() {
    this.lastUpdate = System.currentTimeMillis();
    this.value = updater.get();
  }

  public void forceUpdate(V value) {
    this.lastUpdate = System.currentTimeMillis();
    this.value = value;
  }

  public V get() {
    if (lastUpdate + ttl < System.currentTimeMillis()) update();
    return value;
  }

}
