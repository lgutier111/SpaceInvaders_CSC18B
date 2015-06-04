package SpaceInvaders_V4.TileMap;

import SpaceInvaders_V4.Entity.EnemyEntity;
import SpaceInvaders_V4.Entity.Entity;
import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Main.ResourceFactory;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EntityMap {

    private float x;
    private float y;
    private int mapHeight;
    private int mapWidth;
    private TileMap map;
    private EnemyList enemies;
    private GameState game;

    private JSONObject JSONref;

    private int fWidth;
    private int fHeight;

    public EntityMap(GameState game, TileMap map, String ref) {
        this.map = map;
        mapHeight = map.getHeight();
        mapWidth = map.getWidth();

        fWidth = ResourceFactory.get().getGameWindow().getWidth();
        fHeight = ResourceFactory.get().getGameWindow().getHeight();

        enemies = new EnemyList();
        

        this.game = game;
        //call function to load enemy may data
        loadData(ref);
    }

    private void loadData(String ref) {
        //load Json Object from file
        JSONref = parseJSON(ref);
        
        //get enemy array from json object
        JSONArray jsonEnemyArray = (JSONArray) JSONref.get("EnemyList");
//        System.out.println(jsonEnemyArray);

        //loop through array and add each index as in enemyEntry
        if (jsonEnemyArray.size() > 0) {
            for (int i = 0; i < jsonEnemyArray.size(); i++) {
                JSONObject je = (JSONObject) jsonEnemyArray.get(i);
                enemies.getList().add(new EnemyEntry(
                        this,
                        (int) (long)je.get("spawnTrigger"),
                        (String) je.get("enemyClassName"),
                        (int) (long)je.get("rank"),
                        (int) (long)je.get("spawnX"),
                        (int) (long)je.get("spawnY"),
                        (String) je.get("spawnRef")
                ));
                
            }
        }


//        enemies.getList().add(new EnemyEntry(
//                this,//enemyMap
//                700,//spawnTrigger
//                "EnemyDart",//className
//                1,//rank
//                300,//spawnX
//                -100,//spawnY
//                ""//spawnRef
//        ));
//        enemies.getList().add(new EnemyEntry(
//                this,//enemyMap
//                700,//spawnTrigger
//                "EnemyDart",//className
//                1,//rank
//                400,//spawnX
//                -100,//spawnY
//                ""//spawnRef
//        ));
//        enemies.getList().add(new EnemyEntry(
//                this,//enemyMap
//                700,//spawnTrigger
//                "EnemyDart",//className
//                1,//rank
//                500,//spawnX
//                -100,//spawnY
//                ""//spawnRef
//        ));
//        enemies.getList().add(new EnemyEntry(
//                this,//enemyMap
//                700,//spawnTrigger
//                "EnemyDart",//className
//                1,//rank
//                600,//spawnX
//                -100,//spawnY
//                ""//spawnRef
//        ));
    }

    public float getX() {
        return x;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public GameState getGame() {
        return game;
    }

    //upate EntityMap
    public void update(double delta) {
        //sync to tilemap position
        this.x = map.getX();
        this.y = map.getY();

//        System.out.println("Trigger lv :" + (y + mapHeight));

        //check enemy list to see if there anre any enmies to spawn
        for (int i = 0; i < enemies.getList().size(); i++) {
            //;ist of entries to remove once spawned
            ArrayList removeList = new ArrayList();

            //if the top of the frame reaches the spawn trigger
            if (enemies.getList().get(i).getSpawnTrigger() < (y + mapHeight)) {
                //get Enemy entry
                EnemyEntry entry = enemies.getList().get(i);

                //spawn enemy enyty and add it to the game EnemyArrayList
                EnemyEntity enemy = entry.spawn();
                game.getEnemyEntities().add(enemy);
//                System.out.println("Pop "+enemy.toString());

                //add to entry removal list
                removeList.add(entry);

            }
            //remove spawned entries from queue
            enemies.getList().removeAll(removeList);
            removeList.clear();
        }

    }

    public void draw() {

        // cycle round drawing all the enemy entities we have in the game
        for (int i = 0; i < game.getEnemyEntities().size(); i++) {
            Entity entity = (Entity) game.getEnemyEntities().get(i);
            entity.draw();
        }
    }

    private JSONObject parseJSON(String ref) {
        JSONParser parser = new JSONParser();
        JSONObject jObj = new JSONObject();

        try {
            Object obj = parser.parse(new FileReader(ref));
            jObj = (JSONObject) obj;

        } catch (IOException | ParseException ex) {
            Logger.getLogger(EntityMap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jObj;

    }
}

//enemylist class
class EnemyList {

    private ArrayList<EnemyEntry> list;

    public EnemyList() {
        list = new ArrayList<>();
    }

    public ArrayList<EnemyEntry> getList() {
        return list;
    }

}

