package com.toutiao.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by snowWave on 2017/6/2
 */
public class EventModel {
    private EventType type;
    private int actorId;    //触发者
    private int entityId;   //触发对象
    private int entityType; //触发对象
    private int entityOwnerId;      //拥有者
    private Map<String, String> exts = new HashMap<>(); //拓展信息，参数

    public Map<String, String> getExts() {
        return exts;
    }
    public EventModel() {             //构造函数

    }
    public EventModel(EventType type)       //构造函数
    {
        this.type = type;
    }

    public String getExt(String name) {
        return exts.get(name);
    }

    public EventModel setExt(String name, String value) {
        exts.put(name, value);
        return this;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }
}
