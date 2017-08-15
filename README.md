# Goofer
*Generator Of One-liners From Examples with Ratings*

This is a framework for generating humour from examples.
It was created for my master's thesis *"Automatic Joke Generation: Learning Humour from Examples"*.

An implementation of a system relying on this framework is also provided, namely GAG.
GAG, *Generalised Analogy Generator*, generates "I like my X like I like my Y, Z" jokes from rated examples.
The training data set was collected with our platform, [JokeJudger.com](http://jokejudger.com).
The [implementation of JokeJudger](https://github.com/TWinters/JokeJudger) as well as the [collected data](https://github.com/TWinters/JokeJudger-Data) are also made available.

## Deploy Generalised Analogy Generator

1. **Setting up Java environment:**
In order for GAG to work, [Java 8 SE](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
and [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) needs to be installed.

2. **Setting up required Google N-gram databases:**
In order for GAG to work, [Google Ngrams](https://storage.googleapis.com/books/ngrams/books/datasetsv2.html) needs to be present
in a MySQL database.
More specifically, both English One Million 1-gram and 2-gram needs to be loaded in
using our [Java Google Ngram to MySQL tool](https://github.com/TWinters/google-ngrams-to-mysql) in a database following database
design specified in the repository. Loading this database will take several hours.

3. **Download required repositories**
Aside from [our Google Ngram to MySQL converter tool](https://github.com/TWinters/google-ngrams-to-mysql), this framework is also dependent on [our Markov library](https://github.com/TWinters/Markov) and [our DatamuseAPI Java library](https://github.com/TWinters/Datamuse-Java). They should all be cloned and added as a dependency to the project.

You also need to install [Maven](https://maven.apache.org/). After installing Maven, it will download the remaining required dependencies specified in `pom.xml`.


4. **Running GAG system:**
The GAG system can be executed by using Java to run the main method of the `GeneralisedAnalogyGenerator.java` class.
It supports following arguments:

| Argument            | Description               |
| ------------------- |---------------------------|
|-outputModel | Path where the program should output the training model file |
|-output | Path where the program should output the training model file |
|-maxSimilarity | If given, GAG will only output generations if it differs enough (no more words similar than this value) from previous generations |
|-outputWords | Allow the template values in the training model file (classifiers have diffulty dealing with strings though) |
|-inputJokes | Path to the input jokes file |
|-sortRating| Whether or not the output should be sorted by their rating |
|-minScore | Minimal score threshold to be considered a good joke |
|-sqlHost | Host of the SQL database of the n-grams database |
|-sqlPost | Port of the SQL database of the n-grams database |
|-sqlUser | Username of the SQL database of the n-grams database |
|-sqlPassword | Password of the SQL database of the n-grams database |
|-sqlDB | Database name of the SQL database of the n-grams database |
|-dictionary | Path to the WordNet dictionary |
|-posFile | Path to the Stanford POS tagger |
|-classifier | The classifier to use to learn from the input jokes |
|-aggregator | The rating aggregator to combine the ratings with |
|-x | First template value of an analogy joke |
|-y | Second template value of an analogy joke |
|-z | Third template value of an analogy joke |
|-generator, -g | Type of template values generator: \textit{sql}, \textit{datamuse} or \textit{twogram} |
