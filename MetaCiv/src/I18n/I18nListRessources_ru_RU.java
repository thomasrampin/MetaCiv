package I18n;

import java.util.ListResourceBundle;

public class I18nListRessources_ru_RU extends ListResourceBundle { 
	  public Object[][] getContents() { 
		    return contents; 
		  } 

		  //tableau des mots clÃ©s et des valeurs 

		  static final Object[][] contents = { 
			  {"Annuler","Отмена"},
			  {"Solo / Editeur","Oдиночная игра / Pедактор"},
			  {"Solo", "Oдиночная игра"}, 
			  {"Editeur", "Pедактор"},
			  {"Heberger une partie", "Создать игру"},
			  {"Rejoindre une partie", "Присоединиться к игре"},
			  {"Civkraft : Jeu multijoueur", "Civkraft : Mультиплеер"}, 
			  {"Choix de Langues", "Bыбор языка"},
			  {"Scratch" , "Царапина" },
			  {"<html>Recherche de Serveurs Civkraft dans le réseau local ...<br>Vous pouvez lancer un serveur en choisissant l'option héberger partie" , "<html>Поиск Civkraft серверов в локальной сети ...<br>Вы можете запустить сервер выбрав опцию создать партию"}, 
			  {"Recherche...", "Поиск..."},
			  {"Envoi Schéma Cognitif", "Отправка когнитивной схемы"},
			  {"<html>Vous êtes actuellement connecté au serveur civkraft suivant.<br>En attente du lancement de la partie côté serveur...</html>", "<html>Вы в настоящее время подключены к следующему серверу civkraft.<br>В ожидание запуска игры  со стороны сервера...</html>"},
			  {"<html><center>Veuillez sélectionnez le schéma cognitif <br> que vous désirez envoyer au serveur d'adresse <br>","<html><center>Пожалуйста, выберите когнитивн схему , <br> которую вы хотите отправить на адрес сервера <br>"},
			  {"Serveur", "Север"},
			  {"Serveur démarré avec succès, en attente de joueurs...", "Сервер запустился успешно, в ожидании игроков..."},
			  {"Emplacement Vide","Пустое место"},
			  {"Recherche en cours...", "Поиск..."},
			  //turtlekit.agr
			  {"Kernel","Ядро"},
			  {"Nom","Имя"},
			  {"Lancer la partie","Начать Игру"},
			  {"model","модель"},
			  {"engine", "двигатель"},
			  {"turtles", "черепахи"},
			  {"scheduler","планировщик"},
			  {"viewer","зритель"},
			  {"launch", "запуск" },
			  //turtlekit.gui
			  {"Config","конфиг"},
			  {"Models","модели"},
			  {"Configs","Конфиги"},
			  {"XML Models","XML Модели"},
			//civilisation.inspecteur
	  			//FenetreInspecteur.java
	  			{"Agents","Агенты"},
	  			{"Options","Опции"},
	  			//PanelAgentData.java
	  			{"Bar Chart Demo" , "Гистограмма Демо"},
	  			{"Category", "Категория"},
	  			{"Value", "Значение"},
	  			//PanelAgentLog.java ------- test
	  			{"tick" , "тик"},
	  			//PanelGroupViewer.java
	  			{"Groups","Группы"},
	  			{"Show in world view","Показать в мировоззрении"},
	  			//PanelInspecteur.java ------------------------------------------Test
	  			{"Variable","Переменная"},
	  			//PanelInventaire.java
	  			{"Inventaire","Инвентарь"},
	  			{"Objet","Объект"},
	  			{"Nombre","Количество"},
	  			//PanelListeCognitons.java
	  			{"Cognitons","Когнитоны"},
	  			//PanelListePlans.java
	  			{"Plans","Планы"},
	  			{"poids" , "вес"},
	  			//PanelMindData.java
	  			{"Plans weight","Планы веса"},
	  			{"Cogni weight","Вес Когнитона "},
	  			//PanelOptions.java
	  			{"Affichage des plans : ", "Посмотреть планы : " },
	  			{"Phero map : ","Карта феромонов : "},
	  			{"Show specific group and role :","Показать конкретную группу и роль :"},
	  			{"Aucun patch selectionne", "Нет выбранного патча"},
	  			{"Attribut","Атрибут"},
	  			{"Valeur", "Значение"},
	  			//PanelPerformances
	  			{"OS : ","OS : "},
	  			{"Version : ", "Версия : "},
	  			{"Architecture : ", "Архитектура : "},
	  			{"Total memory: ","Общий объем памяти: "},
	  			{"Used memory : ","Используемая память : "},
	  		//civilisation.inspecteur.advancedcharts
	  			//AdvSubPanelDisplaySelection.java
	  			{"Civilisations" ,"Цивилизации"},
	  			{"choose Civilisations","выбрать цивилизации"},
	  			//ChartWindow.java
	  			{"Population","Население"},
	  			{"Mind","Разум"},
	  			{"Selection","Выбор"},
	  		//civilisation.inspecteur.simulation
	  			//ActionStructureCognitive
	  			{"NewCognitiveScheme","НоваяКогнитивнаяСхема"},
	  			{"Copy This Cognitive Scheme","Скопируйте этот когнитивную схему"},
	  			{"Name :" ,"Имя :"},
	  			{"Create Cognitive Scheme","Создать когнитивную схему"},
	  			{"Are you sure you want to delete this cognitive scheme?","Вы уверены, что хотите удалить эту когнитивную схему?"},
	  			{"DELETE COGNITIVE SCHEME","УДАЛИТЬ когнитивную схему"},
	  			//PanelArbreActions.java
	  			{"Action", "Действие"},
	  			{"Edit", "Редактировать"},
	  			{"Add after", "Добавить после"},
	  			{"Add before", "Добавить до"},
	  			{"Add internal", "Добавить внутреннию"},
	  			{"Remove action", "Удалить действие"},
	  			//PanelModificationSimulation.java
	  			{"Save", "Сохранить"},
	  			{"Make a copy of current save", "Сделайте копию текущего сохранить"},
	  			{"Edit cognitive scheme", "Редактировать когнитивную схему"},
	  			{"Edit environment","Редактировать среду"},
	  			{"Edit item","Редактировать элемент"},
	  			{"Edit civilizations","Редактировать цивилизации"},
	  			{"Edit attributes","Редактировать атрибуты"},
	  			{"Manage group","Управление группой"},
	  			{"Edit amenagement","Редактировать оборудование"},
	  			{"Edit configuration","Редактировать настройки"},
	  			{"Edit constants","Редактировать константы"},
	  			{"quitter","выход"},
	  			{"Action tree", "Действие дерево"},
	  			{"Cognitons and plans", "когнитоны и планы"},
	  			{"Environment","Окружающая среда "},
	  			{"Patch type","Тип патча"},
	  			{"Group manager","Управление группой"},
	  			{"Group tree","Группа дерево"},
	  			{"create cognitive scheme", "создать когнитивную схему"},
	  			{"import cognitive scheme","импорт  когнитивной схемы"},
	  			{"delete cognitive scheme","удалить когнитивной схему"},
	  			{"Save this environment", "Сохранить окружающую среду"},
	  			{"Load an environment","Загрузите среду"},
	  			{"Set environment bounds", "Установить границы среды"},
	  			{"Generate an environment from an existing picture", "Создание среды из существующей картинки"},
	  			{"Pen","Ручка"},
	  			{"Pinceau","Кисть"},
	  			{"Paint bucket","Ведро краски"},
	  			{"Zoom","Увеличить"},
	  			{"Dezoom","Уменьшить"},
	  			{"Choose environment to use for simulation","Выберите среду, чтобы использовать для моделирования"},
	  			{"Manage pheromon","Manage pheromon"},
	  			{"Add a new action at the start of the plan","Добавить новое действие в начале плана"},
	  			{"Create new item","Создать новый элемент"},
	  			{"Create new amenagement","Создание новых макетов"},
	  			{"Create new civilization", "Создать новую цивилизацию"},
	  			//PanelStructureCognitive.java
	  			{"Plan","План"},
	  			{"Edit Plan","Редактировать План"},
	  			{"Remove","Удалить"},
	  			{"Cogniton","Когнитон"},
	  			{"Edit Cogniton","Редактировать Когнитон"},
	  			{"Edit influence links","Редактировать влияние ссылок"},
	  			{"Edit conditional links","Редактировать условные ссылки"},
	  			{"Edit triggering attributes","Редактировать атрибуты запуска"},
	  		//civilisation.inspecteur.simulation.amenagement
	  			//PanelAmenagement.java
	  			{"Facility editor","Редактор фонд"},
	  			{"Add effect : ","Добавить эффект : "},
	  			{"Add this effect","Добавить этот  эффект"},
	  			{"Amenagement name : ","Ремонт макета : "},
	  			{"Icon :","Иконка :"},
	  			{"Description :","Описание :"},
	  			{"Enter facility Description here","Введите описание объекта здесь"},
	  			{"Facility color : ","Цвет объекта : "},
	  			{"Change color","Изменить цвет"},
	  			{"New effect","Новый эффект"},
	  			{"save amenagement","Сохранить Макеты"},
	  			{"Recipe","Recipe"},
	  			//PanelEffectA.java
	  			{"Effect Name : ","Название эффекта : "},
	  			{"Target type : ","Тип цели : "},
	  			{"Attributes","Атрибуты"},
	  			{"Objets","Объекты"},
	  			{"Ressource/tick","Ресурс/тик"},
	  			{"Effect Type : ","Тип Влияние : "},
	  			{"Set","Установить"},
	  			{"Add","Добавить"},
	  			{"Value : ","Значение : "},
	  			{"Activation : ","Активация : "},
	  			{"Possession","Владение"},
	  			{"Use","использование"},
	  			{"Permanent ? : ","Постоянная ? : "},
	  			{"Yes","Да"},
	  			{"No","Нет"},
	  			{"Save Effect","Сохранить Эффект"},
	  			{"Remove Effect","Удалить эффект"},
	  			{"Ressources/tick","Ресурсы/тик"},
	  			{"Target Name : ","имя цели : "},
	  			//PanelListeAmenagement.java
	  			{"Amenagement list","Список Макетов"},
	  			//PanelRecetteA.java
	  			{"Add item for craft","Добавить макет для  изготовления"},
	  			{"Save Recipe","Сохранить Рецепт"},
	  		//civilisation.inspecteur.simulation.dialogues
	  			//DialogChangeGroupName.java
	  			{"group_name","название_группы"},
	  			{"Rename a group","Переименовать группу"},
	  			//DialogChangeRoleName.java
	  			{"role_name", "имя_роли"},
	  			{"Rename a role","Переименование роль"},
	  			//DialogCreatePheromon.java
	  			{"Attribute Name : ","Имя атрибута : "},
	  			{"Start Value : ", "Начальное значение : " },
	  			{"Growing value (units/tick) : ","Растущая стоимость (единиц / тик) : "},
	  			//DialogCreateRole.Java
	  			{"Create a role","Создать роль"},
	  			//DialogEditBounds.java
	  			{"Edit environment bounds","Границы Редактировать окружающей среды"},
	  			//DialogEditPheromon.java
	  			{"Edit pheromons","Редактировать феромоны"},
	  			{"Constant","Постоянная"},
	  			{"Edit triggering attributes","Редактировать атрибуты запуска"},
	  			//DialogSelectEnvironmentToLoad.java
	  			{"Load environment","Загрузить среду"},
	  			//DialogueAjouterAction.java
	  			{"Add new action","Добавить новое действие"},
	  			//DialogueChoisirCouleurAmenagement.java
	  			{"Choisir la couleur", "Выберите цвет"},
	  			//DialogueEditerCogniton.java
	  			{"Cogniton type :","Тип Когнитона :"},
	  			{"Cogniton name :","Имя Когнитона :"},
	  			{"Starting cogniton","Запустить Когнитон"},
	  			{"Give this cogniton to new agents?","Дать этот Когнитон новым агентам?"},
	  			{"Starting apparition chance :","Запустить  появления шанса :"},
	  			{"Cogniton hues : "," Оттенки Когнитона : "},
	  			{"Editer un cogniton","Редактировать Когнитона"},
	  			//DialogueEditerLiensConditionnels.java
	  			{"Conditionned plan :","Обусловленный план :"},
	  			//DialogueEditerPlan.java
	  			{"Auto-Plan","Авто план"},
	  			{"Every agents will run this plan every tick if this box is checked. You could use this features to create automatic cognitons transmissions," ,"Каждый агент будет работать на этом плане каждый тик, если эта каробка будет проверена. Вы могли бы использовать эти возможности для создания Когнитона автоматических трансмиссий,"},
	  			{" or change attributes (need for food...)"," или изменения атрибутов (потребность в пище ...)"},
	  			{"Birth-Plan","План-рождения"},
	  			{"Every agents will run this plan at birth.","Каждый агент будет работать в этом плане после рождения."},
	  			{"Edite plan","Редактировать план"},
	  			//DialogueEditerTerrain.java
	  			{"Editer le terrain","Редактировать поле"},
	  			{"Passability :","проходимость :"},
	  			{"Impassable?","непроходимость?"},
	  			{"Starting value of ","Стартовая значение "},
	  			{"Starting value : ","Стартовая значение : "},
	  			{"Growth value of ","Значение роста " },
	  			{"Growth value : ","Значение роста : "},
	  			//DialogueEnregistrerEnvironnement.java
	  			{"nom","имя"},	  			
	  			{"Enregistrer un environnement","Сохранение окружающей среды"},
	  			//DialoguePlacerCivilisation.java
	  			{"Choisissez une civilisation","Выберите цивилизацию"},
	  			//DialogueSelectionnerEnvironnementActif.java
	  			{"Enregistrer un environnement","Сохранение окружающей среды"},
	  		//civilisation.inspecteur.simulation.environnement
	  			//PanelTerrains.java
	  			{"Terrain","земля"},
	  			{"Edit terrain", "Редактировать землю " },
	  			{"Change colour","Изменить цвет"},
	  		//civilisation.inspecteur.simulation.groupManager
	  			//GroupToolBar.java
	  			{"Add an existing culturon","Добавить существующий Культурон"},
	  			{"Create a new culturon","Создать новый Культурон"},
	  			{"Remove currently selected role (can't remove the last role)","Удалить  выбранную роль (не возможно удалить последнюю роль)"},
	  			{"Add a new role to this group","Добавление новой роли в этой группе"},
	  			{"Rename the current role","Переименовать текущую роль"},
	  			{"Rename the current group","Переименовать текущую группу"},
	  		//civilisation.inspecteur.simulation.objets
	  			//PanelEffect.java
	  			{"Actions","Действие"},
	  			//PanelListeObjets.java
	  			{"Item list","список элементов"},
	  			//PanelObjets.java
	  			{"Item editor","редактор элементов"},
	  			{"Item name : ", "имя элемента : "},
	  			{"Enter item Description here","Введите пункт описание здесь"},
	  			{"save object","сохранить объект"},
	  		//civilisation.inspecteur.viewer
	  			//ViewerTabbed.java
	  			{"Simulation","Моделирование"},
	  			{"Agent","Агент"},
	  			{"Options","Опции"},
	  			{"Performances","Производительность"},
	  			{"Tableau de bord","Приборная панель"},
	  			{"Charts","Чартеры"},
	  			{"Legend","Легенда"},
	  		//java.awt.event.KeyEvent
	  			//TKMenujava
	  			{"TurtleKit","Комплект-черепаха"},
		  }; 
		
}
