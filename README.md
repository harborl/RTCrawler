### Abstract
It is a domain oriented real time `web crawler` that is different than the common `web crawler`.
The later, typically, is link oriented, which crawls all of links and its content in a breath first way.
However, a domain oriented `web crawler` has bellow properties:
 - Real time: it provides a real time method to crawl content.
 - It's naturally 'Awaitable' in that it always has the definite job limitation.
 - Returns domain object but plain HTML.

### File Instruction
- crwaler.sh : It uses the `java` dev tool to run the `Crawler.java` code directly.
- wrapper.sh : It wrappers the `crwaler.sh` to launch multi-properties crawler Linux/Unix `process`.
- run.sh : It's a init-style script to launch the `wrapper.sh` script according the might prompt.

### Precondition
- Creates a `lib` folder and copies all dependency jars to this folder.
- Creates a `logs` folder for logging outputting.
- Creates a `output` folder for result outputting.

### How To Run
Generally, you just need following input in the shell:<br/>
`$ sh run.sh` <br/>
`$ make your choice according the might prompt`
