// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package eu.zomtec.em2012.domain;

import eu.zomtec.em2012.domain.GameGroup;

privileged aspect GameGroup_Roo_JavaBean {
    
    public String GameGroup.getName() {
        return this.name;
    }
    
    public void GameGroup.setName(String name) {
        this.name = name;
    }
    
    public Integer GameGroup.getSortOrder() {
        return this.sortOrder;
    }
    
    public void GameGroup.setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Integer GameGroup.getFactor() {
        return this.factor;
    }
    
    public void GameGroup.setFactor(Integer factor) {
        this.factor = factor;
    }
    
}