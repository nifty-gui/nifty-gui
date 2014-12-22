Nifty 2.0 - You haven't seen anything yet ...

But in case you want to see something. Try something like that:

```
cd /tmp
git clone git://github.com/void256/nifty-gui.git
cd nifty-gui/
git checkout 2.0
mvn clean install
cd nifty-examples/
export MAVEN_OPTS=-Djava.library.path=target/natives/
mvn exec:java -Dexec.mainClass="de.lessvoid.nifty.examples.usecase.UseCase_a05_RotatingChildNode"
```

Also take a look at [the other examples](https://github.com/void256/nifty-gui/tree/2.0/nifty-examples/src/main/java/de/lessvoid/nifty/examples/usecase) which you can run the same way.

Nifty? :smile:

---

* [git commit comment guidelines](CONTRIBUTING.markdown)

