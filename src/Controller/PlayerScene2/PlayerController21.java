package Controller.PlayerScene2;

import Controller.*;
import Controller.Book.BookController;
import Controller.Enemy.BirdController;
import Controller.Enemy.EnemyController;
import Controller.Gift.GiftController;
import Controller.Weapon.WeaponController;
import Model.Bullet;
import Model.Player;
import Utils.Utils;
import View.GameDrawer;
import View.ImageDrawer;

import java.awt.*;

/**
 * Created by giaqu on 8/14/2016.
 */
public class PlayerController21 extends SingleController implements Colliable {

    private static final int SPEED = 10;
    private static final int JUMP_SPEED = 5;
    private static final int ATK_SPEED = 3;
    private int INVISIBLE_COOLDOWN = 5;
    private int count;

    private GameInput gameInput;
    private ControllerManager bulletManager;
    private PlayerController2State playerController2State;

    private PlayerController21(Player player, GameDrawer gameDrawer) {
        super(player, gameDrawer);
        this.gameInput = new GameInput();
        this.bulletManager = new ControllerManager();
        playerController2State = PlayerController2State.NORMAL;
        CollisionPool.instance.add(this);
        //PlayerControllerManager1.instance.add(this);
        //this.gameVector.dx = SPEED;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        bulletManager.draw(g);

    }

    @Override
    public void run() {
        count++;
        this.gameVector.dx = 0;
        switch (playerController2State){
            case NORMAL:
                break;
            case INVISIBLE:
                INVISIBLE_COOLDOWN--;
                if (INVISIBLE_COOLDOWN >= 0) {
                    this.setGameDrawer(new ImageDrawer("resources/nam.png"));
                }else{
                    playerController2State = PlayerController2State.NORMAL;
                }
                break;
        }
        if (gameInput.keyA && !gameInput.keyD) {
            this.gameVector.dx = -SPEED;
            if(this.gameObject.getX() < 50){
                this.gameObject.setX(50);
            }
        } else if (!gameInput.keyA && gameInput.keyD) {
            this.gameVector.dx = SPEED;
            if(this.gameObject.getX() > 1000){
                this.gameObject.setX(1000);
            }
        }
        if (gameInput.keyW) {
            this.gameVector.dy = -JUMP_SPEED;
        }
//        if (gameInput.keyW && gameInput.keyA){
//            this.gameVector.dy -= JUMP_SPEED;
//            this.gameVector.dx -= SPEED;
//
//
//        } else if (gameInput.keyW && gameInput.keyD) {
//            this.gameVector.dy -= JUMP_SPEED;
//            this.gameVector.dx = SPEED;
//        }
//        } else if (gameInput.keyW && !gameInput.keyA){
//            this.gameVector.dy -= JUMP_SPEED;
//        } else if (!gameInput.keyW && gameInput.keyA){
//            this.gameVector.dx -= SPEED;
//        }
        if (gameInput.keySpace) {
            bulletrun();
        }
        if(this.gameObject.getY() >= 200){
            this.gameObject.setY(200);

        } else if (this.gameObject.getY() == 100 ) {
            Utils.playSound("resources/jumpsound.wav", false);
            this.gameVector.dy = JUMP_SPEED;
            gameInput.keyW = false;
        }
        else if (this.gameObject.getX() <= 50){
            this.gameObject.setX(50);
        }else if (this.gameObject.getX() >= 1000){
            this.gameObject.setX(1000);
        }
        super.run();
        bulletManager.run();
    }

    public static PlayerController21 instance = new PlayerController21(new Player(500, 200), new ImageDrawer("resources/ninja1.png"));

    @Override
    public void onCollide(Colliable colliable) {
        if (colliable instanceof BookController) {
            playerController2State = PlayerController2State.INVISIBLE;
            INVISIBLE_COOLDOWN += 5;
            colliable.getGameObject().destroy();
        }
        if(playerController2State == PlayerController2State.INVISIBLE){

        } else if (colliable instanceof WeaponController) {
            PlayerController21.instance.getGameObject().setHp(PlayerController21.instance.gameObject.getHp() - 0);
            colliable.getGameObject().destroy();

        }else if (colliable instanceof EnemyController) {
            colliable.getGameObject().destroy();
            PlayerController21.instance.getGameObject().setHp(PlayerController21.instance.gameObject.getHp() - 0);
        }else if (colliable instanceof BirdController) {
            colliable.getGameObject().destroy();
//            PlayerController21.instance.getGameObject().setHp(PlayerController21.instance.gameObject.getHp() - 1);
        } else if (colliable instanceof GiftController) {
            PlayerController21.instance.getGameObject().setHp(PlayerController21.instance.gameObject.getHp() + 2);
            colliable.getGameObject().destroy();
        }

        if(PlayerController21.instance.gameObject.getHp() <= 0){
            this.getGameObject().destroy();
            PlayerController21.instance.getGameObject().setHp(0);
        }
    }

    public void setGameInput(GameInput gameInput) {
        this.gameInput = gameInput;
    }

    private void bulletrun() {
        if (count > ATK_SPEED) {
            BulletController bulletController = new BulletController(
                    new Bullet(this.gameObject.getMiddleX() - Bullet.WIDTH / 2, this.gameObject.getY()),
                    new ImageDrawer("resources/star.png"));
            bulletManager.add(bulletController);
            count = 0;
            Utils.playSound("resources/shootsound.wav", false);
            //System.out.println("ban");
        }
    }

    public void moveLeft(){
        this.gameInput.keyLeft = true;
        this.gameVector.dy = 0;
    }

    public void moveUp(){
        this.gameInput.keyUp = true;
        this.gameVector.dx = 0;
    }

    public void moveRight(){
        this.gameInput.keyRight = true;
        this.gameVector.dy = 0;
    }

}