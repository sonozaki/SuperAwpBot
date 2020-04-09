# SuperAwpBot

![github](https://i.ibb.co/nD9Xhpx/Super-Awp-Bot.gif)

## ¿Qué es SuperAwpBot?

SuperAwpBot es un ejemplo muy sencillo de como crear un Hack/Cheat para CSGO en Java. Existen muchísimos alrededor de internet, pero quería realizar el Cheat más sencillo posible.

## ¿Cómo instalar SuperAwpBot?

Para que funcione correctamente SuperAwpBot, debes descargar la biblioteca JnativeHook (.jar) de su web oficial e incluirlo en tu jdk/jre.

Una vez instalada dicha biblioteca, es tan sencillo como ejecutar "ejecutar SuperAwpBot.bat" y seguir las instrucciones que aparecen en pantalla, 
mientras la pantalla negra siga abierta, SuperAwpBot seguira funcionando, se puede cerrar cerrando dicha pantalla como cualquier ventana de Windows.

Puedes configurar que tecla será se usará activar SuperAwpBot en el fichero "config.txt"

## ¿Cómo usar SuperAwpBot?

Una vez ejecutado SuperAwpBot debes apuntar con Awp en un pixel donde creas que debe pasar el enemigo, una vez hecho esto, pulsa la tecla del teclado/ratón que tengas indicada en tu fichero "config.txt" (Por defecto es Alt)
En el momento que pulsas la "tecla mágica" SuperAwpBot comprobará si existe algún cambio en el centro de la pantalla (es decir, en la mira) y si existe un cambio disparará y matará a dicho enemigo.
SuperAwpBot dispara si existe un cambio en la mira, por lo que disparará si pasa tu compañero, si tiran un humo o si simplemente te mueves, por lo que debes permanecer quieto esperando que el enemigo pase por el centro de la pantalla.

## ¿Cómo funciona SuperAwpBot?

SuperAwpBot utiliza una biblioteca (JnativeHook) para capturar eventos de teclado/ratón, una vez se lanza dicho evento, SuperAwpBot realiza una captura de pantalla inicial, después realiza capturas de pantalla constantemente y las compara con la captura de pantalla inicial, si existe un cambio SuperAwpBot disparará por ti y esperará que se vuelva a lanzar el evento.

## ¿Que licencia utiliza SuperAwpBot?

SuperAwpBot utiliza la licencia GPLv3, siéntete libre de crear tu propio fork.

## ¡Advertencia!

No uses SuperAwpBot en servidores oficiales ni en juego competitivo, arruinarás la experiencia de juego y seguramente tu cuenta de CSGO será deshabilitada, este proyecto es simplemente con fines educativos.
