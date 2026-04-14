class Character:
    def __init__(self, name: str, weapon: str):
        self.name = name
        self.weapon_type = weapon

    def set_weapon(self, weapon: str):
        print(f"--- {self.name} меняет тактику ---")
        self.weapon_type = weapon

    def attack(self):
        if self.weapon_type == "melee":
            print(f"{self.name} бьет мечом!")
        elif self.weapon_type == "ranged":
            print(f"{self.name} стреляет из лука!")
        elif self.weapon_type == "magic":
            print(f"{self.name} пускает искры!")
        else:
            print(f"{self.name} не знает, как атаковать...")

if __name__ == "__main__":
    hero = Character("Воин", "melee")
    hero.attack()

    hero.set_weapon("ranged")
    hero.attack()

    hero.set_weapon("magic")
    hero.attack()