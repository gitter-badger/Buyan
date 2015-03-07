# Buyan - CensorshipFreeCommentingPlatform
![](https://github.com/NikolaMandic/brandNameFrontendPrototype/blob/master/media/gr7dex10.gif)
![](https://github.com/NikolaMandic/Buyan/blob/master/media/a8d3d362bb2a50296a5d813f3c686e50.gif)
official mascot
![](https://github.com/NikolaMandic/brandNameFrontendPrototype/blob/master/media/250px-Ivan_Bilibin_126.gif)
official mascot in higher quality
http://i.imgur.com/G8XSYgF.webm
official song so that mascot has something to dance to
http://www.infinitelooper.com/?v=AIkO9E_Wyc0&p=n
warning!!! software in early stage
# background
project started after comments censored my tweet regarding Nazi( Stepinac) that is celebrated as a saint by latin heretic cartel in which I reflected on reports of his hands being red from orthodox blood that he litterally killed with a knife with his own two hands. Blog post introducing Buyan http://blog.nikola.link/#buyan
recently twitter can be perceived as a truth unfriendly place they censored me 3 times as far as I noticed
because of tweets regarding latin heresy and various acts by nation stats coming from jesuit(devil's->since jesuits are godless heretics) doctrine of various fantasies justifying criminal activity
since jesuits and entire latin heretic cartel were/are criminals and they felt need possibly to word their behaviour in evil tricky way in order to shove it down people's throats
you can not have sex to be a virgin :-DD
but some people find it an attractive idea if jesuits tell them it can be done
DAMNED BE THE POPE!
now on freenode also #buyan

github pages will host the project here
http://nikolamandic.github.io/Buyan/app/index.html
gh-pages will be the production branch
this repo speciffically will be try to make an app from another angle and is meant to maybe merge with other repo later on
hopefuly pouchdb backed blockchain mining in browser over webrtc should seal the deal with this repo
<br/>
artwork
==========
Buyan will feature artwork in public domain from prominent artists like <a href="https://en.wikipedia.org/wiki/Ivan_Bilibin">Ива́н Я́ковлевич Били́бин(Ivan Bilibin)</a> and <a href="https://en.wikipedia.org/wiki/Ilya_Repin">Илья́ Ефи́мович Ре́пин(Ilya Repin)</a>
currently there is also screenshot from https://en.wikipedia.org/wiki/Soyuzmultfilm cartoon
<br/>

workflow for this is
to enable clojurescpript autorebuild
`lein cljsbuild auto app` from app directory
to build docker image
`cd dockerPeerJS`
`docker built -t peerjs .`
this will run docker
`docker run -it -p 8000:8000 peerjs  peerjs --port 8000 --key peerjs`
also install npm bower gulp
lightable was used to edit the thing

doc that will be up to date from time to time are here
http://blog.nikola.link/buyan.html
protocol

![](https://github.com/NikolaMandic/brandNameFrontendPrototype/blob/master/media/cdraw.png)
![](https://github.com/NikolaMandic/brandNameFrontendPrototype/blob/master/media/getblock.png)
![](https://github.com/NikolaMandic/brandNameFrontendPrototype/blob/master/media/syncChain.png)
![](https://github.com/NikolaMandic/brandNameFrontendPrototype/blob/master/media/broadcast.png)
protocol
![](https://github.com/NikolaMandic/brandNameFrontendPrototype/blob/master/media/protocol.png)

signaling server:
to use my free openshift account here is openshift server app        https://github.com/NikolaMandic/openshiftpeerjssignalingserver


architecture of the project
it mimics until certain point facebook's flux architecture
decoupled modules pass messages via central point

Buyan will hopefully be user extensible ->>
by intercepting messages you
could hopefully do bunch of stuff from javascript
so without learing architecture and modifying contributing ....
hopefully you could use it as black box or modify it if you want :-)
<pre>

                  +----------+
                  |          |
                  | utilitis |
                  |          |
                  +----------+

                    ^      +
                    |      |
                    |      |
                    +      v
+-------+          +--------+          +-------+
|       | +------⇒ |        | +------⇒ |       |
|  ui   |          | router |          | store |
|       | ⇐------+ |        | ⇐------+ |       |
+-------+          +--------+          +-------+
                     +    ^
                     |    |
                     |    |
                     |    |
                     |    |
                     |    |
                     |    |
                     |    |
                     |    |
                     |    |
                     v    +
                  +----------+
                  |          |
                  | internet |
                  |          |
                  +----------+
</pre>
<pre>
        +-------------------+           +-------------------+
        |                   |           |                   |
        | pubsub            |           | pubsub            |
        |                   |           |                   |
        | util //for macros |           | util //for macros |
        |                   |           |                   |
        +-------------------+           +-------------------+

                 ^                              ^
                 |                              |
                 +                              +

       +-----------------------+     +-----------------------+
       |                       |     |                       |
       | module 1              |     | module 2              |
       |                       |     |                       |
       | references routing f  |     | references routing f  |
       |                       |     |                       |
       +-----------------------+     +-----------------------+

                ^                              ^
                |                              |
                |                              |
                |                              |
                |                              |
                +                              +

       +----------------------------------------------------+
       |                                                    |
       |                                                    |
       |                   router                           |
       |                                                    |
       |            it references all functions             |
       |                                                    |
       +----------------------------------------------------+


                               ^
                               | it references 1 routing f
                               +

                    +-------------------+
                    |                   |
                    | pubsub            |
                    |                   |
                    | util //for macros |
                    |                   |
                    +-------------------+
(c "x" arg1 argx ...) =

module1.f -> pubsub.send -> router.route -> module2.x -> return


</pre>

<pre>
                                                                     
                                                                  
                                                                  
                                                                  
                                                                  
                                                                  
  +---+                                                   +---+   
  |   |                                                   |   |   
  |   |                                                   |   |   
  +-----------------------------------------------------------+   
      |                                                   |       
      |                       ui                          |       
      |                                                   |       
      |     clojurescript        css/js                   |       
      |                                                   |       
      |     +-----------+      +--------------------+     |       
      |     |           |      |                    |     |       
      |     | react     |      | leaf css           |     |       
      |     |           |      | crow grid system   |     |       
      |     | reagent   |      |                    |     |       
      |     |           |      | http://getleaf.com |     |       
      |     +-----------+      |                    |     |       
      |                        | bootstrap          |     |       
      |                        |                    |     |       
      |                        | modernizer         |     |       
      |                        |                    |     |       
      |                        | jquery             |     |       
      |                        |                    |     |       
      |                        +--------------------+     |       
      |                                                   |       
      |                                                   |       
  +-----------------------------------------------------------+   
  |   |                                                   |   |   
  |   |                                                   |   |   
  +---+                                                   +---+   
 
                          

</pre>
<pre>

                                                                                                           
+----------------------------------------------------------------+
|                                                                |
|  window.preroutinghook.fnametohook=                            |
|                                                                |
|               (function(m){                                    |
|                        console.log("prer.hoook ",m);           |
|                                                                |
|                        return ["1"]; }) //we return new args   |
|                                                                |
+------------------------------------------+---------------------+
                                           |
                                           |
                                           |
                                           |
                                           |
+-------------------------+     +----------+------+     +--------+
|                         |     |                 |     |        |
| c "fname" arg1 arg2 ... | +-> |  preroutinghook | +-> | router |
|                         |     |                 |     |        |
+-------------------------+     +-----------------+     +--------+

                                                            +
                                                            v

                               +--------+      +-----------------+
                               |        |      |                 |
                               | return | ⇐--+ | postroutinghook |
                               |        |      |                 |
                               +--------+      +-----------------+





+--------------------------+     +-----------------+     +--------+
|                          |     |                 |     |        |
| ac "fname" arg1 arg2 ... | +-⇒ |  preroutinghook | +-⇒ | router |
|                          |     |                 |     |        |
+--------------------------+     +-----------------+     +--------+

                                                             +
                                                             v

                                                +-----------------+
                                                |                 |
                                                | postroutinghook |
                                                |                 |
                                                +-----------------+


</pre>
<pre>

+------------------------------------------------------------+
| buyan engine                                               |
|                                                            |
|       +---+     +--------------------------------------+   |
|       |   |     |                                      |   |
|       | m | +-> |  pubsub mechanism triggers something |   |
|       |   |     |                                      |   |
|       +---+     +--------------------------------------+   |
|                                                            |
|         ^                                                  |
+------------------------------------------------------------+
          |
+------------------------------------------------------------+
| user    |                                                  |
|       +-+-+                                                |
|       |   |                                                |
|       | m |                                                |
|       |   |                                                |
|       +--++                                                |
|          |                                                 |
+------------------------------------------------------------+
           |
           |
           |
       +---+-------------------+
       |                       |
       | m =                   |
       | {"typ" typ "msg" msg} |
       |                       |
       | $(document).trigger(  |
       | "pubsub",m);          |
       |                       |
       | +------------------+  |
       | |                  |  |
       | | typ type         |  |
       | |                  |  |
       | | msg value for f  |  |
       | |                  |  |
       | +------------------+  |
       |                       |
       +-----------------------+

+------------------------------------------------------------+
| buyan engine                                               |
|                                                            |
|       +---+     +------------------------+                 |
|       |   |     |                        |                 |
|       | m | +-> |  function gets called  |                 |
|       |   |     |                        |                 |
|       +---+     +------------------------+                 |
|                                                            |
|         ^                                                  |
+------------------------------------------------------------+
          |
+------------------------------------------------------------+
| user    |                                                  |
|       +-+-+                                                |
|       |   |                                                |
|       | m |                                                |
|       |   |                                                |
|       +-+-+                                                |
|         |                                                  |
+------------------------------------------------------------+
          |
          |
       +--+--------------------+
       |                       |
       | m =                   |
       | {"typ" typ "msg" msg} |
       |                       |
       | $(document).trigger(  |
       | "call",m);            |
       |                       |
       | +------------------+  |
       | |                  |  |
       | | typ type         |  |
       | |                  |  |
       | | msg value for f  |  |
       | |                  |  |
       | +------------------+  |
       |                       |
       +-----------------------+


</pre>


<br/>
Buyan after
https://www.youtube.com/watch?v=4QtkDMXA0u8
with subtitles here https://www.youtube.com/watch?v=b3TQevs14CE
![](https://github.com/NikolaMandic/brandNameFrontendPrototype/blob/master/media/Ivanbilibin.jpg)

<br/>

Copyright (c) 2014 Nikola Mandic
