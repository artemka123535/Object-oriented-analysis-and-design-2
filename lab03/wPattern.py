from abc import ABC, abstractmethod

class CombatStrategy(ABC):
    @abstractmethod
    def execute_attack(self):
        pass

class MeleeAttack(CombatStrategy):
    def execute_attack(self):
        return "наносит сокрушительный удар мечом в ближнем бою! ⚔️"

class RangedAttack(CombatStrategy):
    def execute_attack(self):
        return "выпускает точную стрелу из длинного лука! 🏹"

class MagicAttack(CombatStrategy):
    def execute_attack(self):
        return "произносит заклинание и выпускает огненный шар! 🔥"

class Character:
    def __init__(self, name: str, strategy: CombatStrategy):
        self.name = name
        self._strategy = strategy

    def set_strategy(self, strategy: CombatStrategy):
        print(f"--- {self.name} меняет тактику ---")
        self._strategy = strategy

    def attack(self):
        result = self._strategy.execute_attack()
        print(f"{self.name} {result}")

if __name__ == "__main__":
    hero = Character("Воин", MeleeAttack())
    hero.attack()

    hero.set_strategy(RangedAttack())
    hero.attack()

    hero.set_strategy(MagicAttack())
    hero.attack()