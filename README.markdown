Nifty 2.0 - You haven't seen anything yet ...

... but in case you want to see something, try:

```
cd /tmp
git clone git://github.com/nifty-gui/nifty-gui.git
cd nifty-gui/
git checkout 2.0
mvn clean install
cd nifty-examples/
export MAVEN_OPTS=-Djava.library.path=target/natives/
mvn exec:java -Dexec.mainClass="de.lessvoid.nifty.examples.usecase.UseCase_a05_RotatingChildNode"
```

Also take a look at [the other examples](https://github.com/void256/nifty-gui/tree/2.0/nifty-examples/src/main/java/de/lessvoid/nifty/examples/usecase) which you can run the same way.

Nifty? :smile:

**Disclaimer: This is really very early work in progress preview kind of stuff! Be careful when you look around .. there might be monsters ...**

---

* [JavaDoc](http://nifty-gui.github.io/nifty-gui/nifty/apidocs/index.html)
* [git commit comment guidelines](CONTRIBUTING.markdown)
* [Roadmap](https://github.com/nifty-gui/nifty-gui/wiki/Nifty-2.x-Roadmap)

