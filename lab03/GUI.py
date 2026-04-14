import tkinter as tk
from tkinter import ttk, messagebox
from abc import ABC, abstractmethod
import time

MONSTER_MAX_HP = 100
CANVAS_WIDTH = 600
CANVAS_HEIGHT = 200
HP_BAR_WIDTH = 50

DAMAGE_MELEE = 10
DAMAGE_RANGED = 20
DAMAGE_MAGIC = 40

class ICombatStrategy(ABC):
    
    @abstractmethod
    def get_name(self) -> str: pass

    @abstractmethod
    def get_damage(self) -> int: pass

    @abstractmethod
    def execute_visual(self, canvas, attacker_id, target_id):
        pass

class MeleeStrategy(ICombatStrategy):
    def get_name(self): return "Меч"
    def get_damage(self): return DAMAGE_MELEE
    
    def execute_visual(self, canvas, attacker_id, target_id):
        orig_coords = canvas.coords(attacker_id)
        target_coords = canvas.coords(target_id)
        
        canvas.move(attacker_id, 80, 0)
        canvas.update()
        time.sleep(0.05)
        slash = canvas.create_line(target_coords[0]-10, target_coords[1]+10, 
                                   target_coords[2]+10, target_coords[3]-10, fill="silver", width=3)
        canvas.update()
        time.sleep(0.1)
        canvas.delete(slash)
        canvas.coords(attacker_id, orig_coords[0], orig_coords[1], orig_coords[2], orig_coords[3])
        canvas.update()

class RangedStrategy(ICombatStrategy):
    def get_name(self): return "Лук"
    def get_damage(self): return DAMAGE_RANGED
    
    def execute_visual(self, canvas, attacker_id, target_id):
        start = canvas.coords(attacker_id)
        target = canvas.coords(target_id)
        arrow = canvas.create_rectangle(start[2], start[1]+10, start[2]+10, start[1]+20, fill="brown")
        
        steps = 20
        for _ in range(steps):
            canvas.move(arrow, (target[0] - start[2])/steps, 0)
            canvas.update()
            time.sleep(0.01)
        canvas.delete(arrow)

class MagicStrategy(ICombatStrategy):
    def get_name(self): return "Файербол"
    def get_damage(self): return DAMAGE_MAGIC
    
    def execute_visual(self, canvas, attacker_id, target_id):
        start = canvas.coords(attacker_id)
        target = canvas.coords(target_id)
        ball = canvas.create_oval(start[2], start[1], start[2]+15, start[1]+15, fill="red", outline="orange")
        
        steps = 15
        for _ in range(steps):
            canvas.move(ball, (target[0] - start[2])/steps, 0)
            canvas.update()
            time.sleep(0.02)
        
        cur_coords = canvas.coords(ball)
        boom = canvas.create_oval(cur_coords[0]-20, cur_coords[1]-20, cur_coords[2]+20, cur_coords[3]+20, fill="orange", outline="yellow")
        canvas.delete(ball)
        canvas.update()
        time.sleep(0.1)
        canvas.delete(boom)
        canvas.update()

class HeroCharacter:
    def __init__(self, name, strategy: ICombatStrategy):
        self.name = name
        self.strategy = strategy
        self.canvas_id = None

    def set_strategy(self, strategy: ICombatStrategy, log_cb):
        self.strategy = strategy
        log_cb(f"*** {self.name} сменил оружие на: {strategy.get_name()} ***")

    def attack(self, world):
        damage = self.strategy.get_damage()
        name = self.strategy.get_name()
        world['log'](f"{self.name} атакует: {name}!")
        
        self.strategy.execute_visual(world['canvas'], self.canvas_id, world['monster_id'])
        
        world['apply_damage'](damage)

class HeroCharacterBad:
    def __init__(self, name):
        self.name = name
        self.weapon_type = "меч"
        self.canvas_id = None

    def set_weapon(self, weapon_str: str, log_cb):
        self.weapon_type = weapon_str
        log_cb(f"*** {self.name} сменил оружие на: {weapon_str} ***")

    def attack(self, world):
        canvas = world['canvas']
        monster_id = world['monster_id']
        attacker_id = self.canvas_id

        
        if self.weapon_type == "меч":
            damage = DAMAGE_MELEE
            world['log'](f"{self.name} атакует: Меч!")
            orig_coords = canvas.coords(attacker_id)
            target_coords = canvas.coords(monster_id)
            canvas.move(attacker_id, 80, 0)
            canvas.update()
            time.sleep(0.05)
            slash = canvas.create_line(target_coords[0]-10, target_coords[1]+10, 
                                       target_coords[2]+10, target_coords[3]-10, fill="silver", width=3)
            canvas.update()
            time.sleep(0.1)
            canvas.delete(slash)
            canvas.coords(attacker_id, orig_coords[0], orig_coords[1], orig_coords[2], orig_coords[3])
            canvas.update()
            
        elif self.weapon_type == "лук":
            damage = DAMAGE_RANGED
            world['log'](f"{self.name} атакует: Лук!")
            start = canvas.coords(attacker_id)
            target = canvas.coords(monster_id)
            arrow = canvas.create_rectangle(start[2], start[1]+10, start[2]+10, start[1]+20, fill="brown")
            steps = 20
            for _ in range(steps):
                canvas.move(arrow, (target[0] - start[2])/steps, 0)
                canvas.update()
                time.sleep(0.01)
            canvas.delete(arrow)
            
        elif self.weapon_type == "магия":
            damage = DAMAGE_MAGIC
            world['log'](f"{self.name} атакует: Файербол!")
            start = canvas.coords(attacker_id)
            target = canvas.coords(monster_id)
            ball = canvas.create_oval(start[2], start[1], start[2]+15, start[1]+15, fill="red", outline="orange")
            steps = 15
            for _ in range(steps):
                canvas.move(ball, (target[0] - start[2])/steps, 0)
                canvas.update()
                time.sleep(0.02)
            cur_coords = canvas.coords(ball)
            boom = canvas.create_oval(cur_coords[0]-20, cur_coords[1]-20, cur_coords[2]+20, cur_coords[3]+20, fill="orange", outline="yellow")
            canvas.delete(ball)
            canvas.update()
            time.sleep(0.1)
            canvas.delete(boom)
            canvas.update()
        else:
            damage = 0
            world['log'](f"{self.name} не знает, как атаковать...")

        world['apply_damage'](damage)

class GameApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Радмир Ренатович против Ромы Эннса")
        self.root.geometry("800x620")

        style = ttk.Style(root)
        try:
            if 'clam' in style.theme_names(): style.theme_use('clam')
        except: pass

        self.notebook = ttk.Notebook(root)
        self.notebook.pack(expand=True, fill='both')

        # Создаем вкладки
        self.setup_game_tab(is_pattern=True)
        self.setup_game_tab(is_pattern=False)

    def setup_game_tab(self, is_pattern):
        frame = ttk.Frame(self.notebook)
        title = " С Паттерном" if is_pattern else " Без Паттерна"
        self.notebook.add(frame, text=title)

        game_state = {'hp': MONSTER_MAX_HP}

        canvas = tk.Canvas(frame, width=CANVAS_WIDTH, height=CANVAS_HEIGHT, bg="#222")
        canvas.pack(pady=10)

        canvas.create_rectangle(0, 160, CANVAS_WIDTH, CANVAS_HEIGHT, fill="#444", outline="")
        
        hero_color = "cyan" if is_pattern else "orange"
        h_id = canvas.create_rectangle(50, 80, 100, 160, fill=hero_color, outline="white")
        h_name = "Радмир Ренатович" if is_pattern else "Радмир Ренатович"
        canvas.create_text(75, 70, text=h_name, fill="white", font=("Arial", 9))
        
        m_id = canvas.create_rectangle(500, 80, 550, 160, fill="red", outline="black")
        canvas.create_text(525, 70, text="Рома Эннс", fill="white", font=("Arial", 9, "bold"))
        
        canvas.create_rectangle(500, 50, 500 + HP_BAR_WIDTH, 60, fill="#333", outline="gray")
        hp_bar = canvas.create_rectangle(500, 50, 500 + HP_BAR_WIDTH, 60, fill="lime", outline="")
        hp_text = canvas.create_text(525, 40, text=f"{MONSTER_MAX_HP}/{MONSTER_MAX_HP}", fill="lime", font=("Arial", 8))
        
        log_text = tk.Text(frame, height=6, width=70, bg="black", fg="white", font=("Consolas", 9))
        log_text.pack(pady=5)
        log_text.insert(tk.END, f"--- Начинается бой ({h_name}) ---\n")

        def apply_damage(amount):
            if game_state['hp'] <= 0: return

            game_state['hp'] -= amount
            if game_state['hp'] < 0: game_state['hp'] = 0
            
            current_hp_ratio = game_state['hp'] / MONSTER_MAX_HP
            new_bar_width = current_hp_ratio * HP_BAR_WIDTH
            canvas.coords(hp_bar, 500, 50, 500 + new_bar_width, 60)
            canvas.itemconfig(hp_text, text=f"{game_state['hp']}/{MONSTER_MAX_HP}")
            
            log_text.insert(tk.END, f"  >> Рома получает урон: -{amount} HP.\n")
            log_text.see(tk.END)
            
            if game_state['hp'] <= 0:
                log_text.insert(tk.END, ">>> РОМА ПОВЕРЖЕН! <<<\n")
                if messagebox.askyesno("Победа", "Рома погиб! Воскресить его?"):
                    game_state['hp'] = MONSTER_MAX_HP
                    canvas.coords(hp_bar, 500, 50, 550, 60)
                    canvas.itemconfig(hp_text, text=f"{MONSTER_MAX_HP}/{MONSTER_MAX_HP}")
                    log_text.insert(tk.END, "-- Рома таинственно воскрес... --\n")

        world = {
            'canvas': canvas,
            'monster_id': m_id,
            'log': lambda m: (log_text.insert(tk.END, m + "\n"), log_text.see(tk.END)),
            'apply_damage': apply_damage
        }

        def log_cb(msg):
            log_text.insert(tk.END, msg + "\n")
            log_text.see(tk.END)

        if is_pattern:
            hero = HeroCharacter(h_name, MeleeStrategy())
        else:
            hero = HeroCharacterBad(h_name)
        hero.canvas_id = h_id

        ctrl_frame = ttk.LabelFrame(frame, text=" Управление персонажем ")
        ctrl_frame.pack(pady=10, fill='x', padx=20)
        
        change_frame = ttk.Frame(ctrl_frame)
        change_frame.pack(side=tk.LEFT, padx=10)
        
        ttk.Label(change_frame, text="Сменить тактику:", font=("Arial", 9)).pack(anchor='w')

        if is_pattern:
            ttk.Button(change_frame, text="Меч", command=lambda: hero.set_strategy(MeleeStrategy(), log_cb)).pack(side=tk.LEFT, padx=2)
            ttk.Button(change_frame, text="Лук", command=lambda: hero.set_strategy(RangedStrategy(), log_cb)).pack(side=tk.LEFT, padx=2)
            ttk.Button(change_frame, text="Магия", command=lambda: hero.set_strategy(MagicStrategy(), log_cb)).pack(side=tk.LEFT, padx=2)
        else:
            ttk.Button(change_frame, text="Меч", command=lambda: hero.set_weapon('меч', log_cb)).pack(side=tk.LEFT, padx=2)
            ttk.Button(change_frame, text="Лук", command=lambda: hero.set_weapon('лук', log_cb)).pack(side=tk.LEFT, padx=2)
            ttk.Button(change_frame, text="Магия", command=lambda: hero.set_weapon('магия', log_cb)).pack(side=tk.LEFT, padx=2)
        
        ttk.Button(ctrl_frame, text="АТАКОВАТЬ!", style="Accent.TButton", cursor="hand2",
                   command=lambda: hero.attack(world)).pack(side=tk.RIGHT, padx=20, pady=10)

if __name__ == "__main__":
    root = tk.Tk()
    app = GameApp(root)
    root.mainloop()