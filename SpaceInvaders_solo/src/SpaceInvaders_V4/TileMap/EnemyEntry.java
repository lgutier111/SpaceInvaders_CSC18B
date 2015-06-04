package SpaceInvaders_V4.TileMap;

import SpaceInvaders_V4.Entity.EnemyEntity;
import SpaceInvaders_V4.GameState.GameState;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnemyEntry {
    private EntityMap enemyMap;
    private int spawnTrigger;
    private int rank;
    private int spawnX;
    private int spawnY;
    private String spawnRef;
    private String enemyClassName;

    
    //constructor class
    //@param EntityMap instance into which to spawn entities
    //@param spawnTrigger distance from bottom of map that will trigger spawn of entity
    //@param rank of entity
    //@param spawnX horizontal position of entity spawn
    //@param spawnY vertical position of entity spawn
    //@param spawnRef special Flags
    //@param enemyClassName class name of entity to spawn
    EnemyEntry(EntityMap enemyMap, int spawnTrigger, String enemyClassName, int rank, int spawnX, int spawnY, String spawnRef) {
        this.enemyMap = enemyMap;
        this.spawnTrigger = spawnTrigger > enemyMap.getMapHeight() ? enemyMap.getMapHeight() : spawnTrigger; //set upper bound
        this.enemyClassName = "SpaceInvaders_V4.Entity." + enemyClassName;
        this.rank = rank;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnRef = spawnRef;
    }

    int getSpawnTrigger() {
        return spawnTrigger;
    }

    void setSpawnTrigger(int spawnTrigger) {
        this.spawnTrigger = spawnTrigger;
    }

    int getSpawnX() {
        return spawnX;
    }

    void setSpawnX(int spawnX) {
        this.spawnX = spawnX;
    }

    int getSpawnY() {
        return spawnY;
    }

    void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
    }

    String getSpawnRef() {
        return spawnRef;
    }

    void setSpawnRef(String spawnRef) {
        this.spawnRef = spawnRef;
    }

    EnemyEntity spawn() {
        //create empty class and object
        Class enemyClass;
        EnemyEntity instance = null;
        //        System.out.println(enemyClassName);
        try {
            //get className
            enemyClass = Class.forName(enemyClassName);
            //create instance of that class
            instance = (EnemyEntity) enemyClass.getDeclaredConstructor(GameState.class, int.class, int.class, int.class, String.class).newInstance(enemyMap.getGame(), rank, (int) (spawnX + enemyMap.getX()), spawnY, spawnRef);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            System.out.printf("Class Name = %s\n", enemyClassName);
            Logger.getLogger(EnemyEntry.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return instance;
    }
    
}
