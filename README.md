
# Telepartacyja
Плагін з фунцыяналам для тэлепартацыі гульцоў.

# Features

 - Вы можаце ўсталяваць пункт спаўна, на які гульцы з дазволам могуць тэлепартавацца. 
	 - `/spawn set` — усталяванне пункта спаўна, патрабуе OP.
	 - `/spawn` — запуск працэсса тэлепартацыі на спаўн, патрабуе дазвол `spawn.usage`
 - Праз `config.yml` можна задаць наступныя значэнні:
	 - `teleport.delay` — затрымка перад тэлепартацыяй. Не датычыцца гульцоў з дазволам `spawn.bypass.delay`.
	 - `teleport.cooldown` — час, які гульцы павінны чакаць перад наступным выкарыстаннем тэлепартацыі. Не датычыцца гульцоў з дазволам `spawn.bypass.cooldown`.
	 - `server.tps` — TPS вашага сервера. Выкарыстоўваецца для разліку затрымкі. **Змяняйце, толькі калі дэфолтны TPS вашага сервера не роўны 20.**
	 - Усе радкі з паведамленнямі, якія выкарыстоўваюцца плагінам.
 - Тэлепартацыя будзе скасавана, калі гулец рухаецца падчас чакання на затрымку.
	 - Не датычыцца гульцоў з дазволам `spawn.bypass.cancel-on-move`
 - Пасля смерці гульцы будуць тэлепартаваны на спаўн.
	 - У тым выпадку калі яны не маюць ложка альбо котвы.   
 - Вы можаце ўсталяваць асобны пункт спаўна для новых гульцоў.
	 - `/spawn firstset` — патрабуе OP.

# API

Плагін мае ўласны API, праз які распрацоўшчыкі іншых плагінаў могуць карыстацца функцыяналам тэлепартацыі.
Каб яго выкарыстаць, вам патрэбна імпартаваць Telepartacyja-API-x.x.jar ([Releases](https://github.com/MinecraftBelarusianPlugins/Telepartacyja/releases)) як бібліятэку ва ўласны праэкт.
Атрымаць API-instance можна праз `ServicesManager`:

**Kotlin**

    import by.siarhiejbahdaniec.telepartacyja.api.TelepartacyjaApi
    
    val api = Bukkit.getServicesManager()
	    .getRegistration(TelepartacyjaApi::class.java)
	    ?.provider

**Java**

    import by.siarhiejbahdaniec.telepartacyja.api.TelepartacyjaApi
    
    TelepartacyjaApi api = null;
    RegisteredServiceProvider<TelepartacyjaApi> provider = Bukkit.getServicesManager().getRegistration(TelepartacyjaApi.class);
    
    if (provider != null) {
	    api = provider.getProvider();
   	}
