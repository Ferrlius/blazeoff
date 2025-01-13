package ferrlius.blazeoff;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class RandomScatterParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;

    /**
     * @param world         — мир, в котором рендерится частица
     * @param x             — начальная координата X
     * @param y             — начальная координата Y
     * @param z             — начальная координата Z
     * @param vx            — начальная скорость по оси X
     * @param vy            — начальная скорость по оси Y
     * @param vz            — начальная скорость по оси Z
     */
    public RandomScatterParticle(ClientWorld world,
                                 double x, double y, double z,
                                 double vx, double vy, double vz,
                                 SpriteProvider spriteProvider) {
        super(world, x, y, z, vx, vy, vz);
        this.spriteProvider = spriteProvider;

        this.setSprite(this.spriteProvider);

        // Чтобы частица взаимодействовала с блоками (не проходила сквозь)
        this.collidesWithWorld = true;

        // Начальные скорости
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;

        // Задать начальное время жизни, например, 60 тиков (3 секунды при 20 TPS)
        this.maxAge = 60;

        // Прозрачность (alpha), можно менять для эффекта растворения
        this.setAlpha(1.0f);

        // Немного случайных цветов или зафиксированный цвет
        this.setColor(1.0f, 1.0f, 1.0f);

        // Можно подрегулировать размер (scale). Default = 0.2F
        this.scale = 0.2F;
    }

    @Override
    public void tick() {
        super.tick(); // Вызов базовой логики передвижения и обработки жизни частицы

        // This method will pick the correct frame from the JSON definition
        setSpriteForAge(this.spriteProvider);

        // Добавим "мягкую" гравитацию:
        // допустим, частица падает медленнее, чем обычная.
        float gravityStrength = 0.02f; // можно варьировать
        this.velocityY -= gravityStrength;

        // Если частица "умерла" (maxAge кончился), просто выходим
        if (this.age++ >= this.maxAge) {
            this.markDead();
        }

        // Встроенный метод move() учитывает столкновения и коллизии
        this.move(this.velocityX, this.velocityY, this.velocityZ);

        // При столкновении с поверхностью блоков можно имитировать "скольжение":
        if (this.onGround) {
            // Допустим, при касании земли уменьшаем скорость по X и Z,
            // имитируя трение, но не останавливаем полностью.
            this.velocityX *= 0.7;
            this.velocityY *= 0.7;
            this.velocityZ *= 0.7;

            // Можно также слегка "подпрыгивать":
            // this.velocityY = -this.velocityY * 0.5;
        }

        // Аналогично, если столкнулись со стеной,
        // можно обнулять или отражать X/Z векторы.
        // Для упрощения здесь оставим как есть.
    }

    @Override
    public ParticleTextureSheet getType() {
        // Используем стандартный лист текстур для Billboard-частиц
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
}