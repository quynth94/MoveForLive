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
 * Created by Viet on 8/19/2016.
 */
public class PlayerController22 extends SingleController implements Colliable {
    private static final int SPEED = 10;
    private static final int JUMP_SPEED = 5;
    private static final int ATK_SPEED = 3;
    private int count;
    private int INVISIBLE_COOLDOWN = 5;
    private GameInput gameInput;
    private ControllerManager bulletManager;
    private PlayerController2State playerController2State;

    public PlayerController22(Player gameObject, GameDrawer gameDrawer) {
        super(gameObject, gameDrawer);
        this.gameInput = new GameInput();
        this.bulletManager = new ControllerManager();
        CollisionPool.instance.add(this);
        playerController2State = PlayerController2State.NORMAL;
        //PlayerControllerManager1.instance.add(this);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        bulletManager.draw(g);
    }

    @Override
    public void onCollide(Colliable colliable) {
        if (colliable instanceof BookController) {
            playerController2State = PlayerController2State.INVISIBLE;
            INVISIBLE_COOLDOWN += 5;
            colliable.getGameObject().destroy();
        }
        if(playerController2State == PlayerController2State.INVISIBLE){

        }else if (colliable instanceof WeaponController) {
            PlayerController22.instance.getGameObject().setHp(PlayerController22.instance.gameObject.getHp() - 1);
            colliable.getGameObject().destroy();
        }else if (colliable instanceof EnemyController) {
            colliable.getGameObject().destroy();
            PlayerController22.instance.getGameObject().setHp(PlayerController22.instance.gameObject.getHp() - 1);
        }else if (colliable instanceof BirdController) {
            colliable.getGameObject().destroy();
//            PlayerController22.instance.getGameObject().setHp(PlayerController22.instance.gameObject.getHp() - 1);
        }else if (colliable instanceof GiftController) {
            PlayerController22.instance.getGameObject().setHp(PlayerController22.instance.gameObject.getHp() + 2);
            colliable.getGameObject().destroy();
        }else if(PlayerController22.instance.gameObject.getHp() <= 0){
            this.getGameObject().destroy();
            PlayerController22.instance.getGameObject().setHp(0);
        }

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
                    this.setGameDrawer(new ImageDrawer("resources/nu.png"));
                }else{
                    playerController2State = PlayerController2State.NORMAL;
                }
                break;
        }
        if (gameInput.keyLeft&& !gameInput.keyRight) {
            this.gameVector.dx = -SPEED;
            if(this.gameObject.getX() < 50){
                this.gameObject.setX(50);
            }
        } else if (!gameInput.keyLeft && gameInput.keyRight) {
            this.gameVector.dx = SPEED;
            if(this.gameObject.getX() > 1000){
                this.gameObject.setX(1000);
            }
        }
        if (gameInput.keyUp) {
            this.gameVector.dy = -JUMP_SPEED;
        }
//        if (gameInput.keyUp && gameInput.keyLeft){
//            this.gameVector.dy -= JUMP_SPEED;
//            this.gameVector.dx -= SPEED;
//
//
//        } else if (gameInput.keyUp && gameInput.keyRight){
//            this.gameVector.dy -= JUMP_SPEED;
//            this.gameVector.dx = SPEED;
//
//        }
        if (gameInput.keyEnter) {
            bulletrun2();
        }
        if(this.gameObject.getY() >= 500){
            this.gameObject.setY(500);

        }else if (this.gameObject.getY() == 400 ) {
            Utils.playSound("resources/jumpsound.wav", false);
            this.gameVector.dy = JUMP_SPEED;
            gameInput.keyUp = false;
        }else if (gameObject.getX() <= 50) {
            this.gameObject.setX(50);
        }else if ( gameObject.getX() >= 1000) {
            this.gameObject.setX(1000);
        }
        this.getGameObject().moveTo(gameObject.getX() + gameVector.dx, gameObject.getY() + gameVector.dy);
        super.run();
        bulletManager.run();
    }

    public final static PlayerController22 instance = new PlayerController22(
            new Player(500, 500),
            new ImageDrawer("resources/ninja2.png")
    );

    private void bulletrun2() {
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
        this.gameInput.keyA = true;
        this.gameVector.dy = 0;
    }

    public void moveRight(){
        this.gameInput.keyD = true;
        this.gameVector.dy = 0;
    }
    public void moveUp(){
        this.gameInput.keyW = true;
        this.gameVector.dx = 0;
    }

    public void stopLeft(){
        this.gameInput.keyA = false;
    }

    public void stopRight(){
        this.gameInput.keyD = false;
    }

    public void setGameInput(GameInput gameInput) {
        this.gameInput = gameInput;
    }
}
