# Buyan - CensorshipFreeCommentingPlatform

[![Join the chat at https://gitter.im/NikolaMandic/Buyan](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/NikolaMandic/Buyan?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
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
project started after comments censored my tweet regarding Nazi( Stepinac) that is celebrated as a saint by latin heretic cartel in which I reflected on reports of his hands being red from orthodox blood that he literally killed with a knife with his own two hands. Blog post introducing motivation behind Buyan http://blog.nikola.link/#buyan
so just like there was Armenian genocide there was genocide in balkans
in wwII against Jews,Roma and Serbs organized by organization people
refer to as "Catholic church".
Here is one of concentration camps where it happened https://en.wikipedia.org/wiki/Jasenovac_concentration_camp        
I've provided that link since many people will most probably not believe that it happened.
Like you have holocaust deniers and people who change history that is how there are people who do not believe this is true.
Latin heretic cartel colloquially known as "Catholic church" is one of the major history falsifiers in entire worlds history. 
What latin heretics do not like is that this thing happened under their blessings and that they celebrate the guy who was among organisers as a saint
"Catholic church" organized this holocaust. From their monastaries those criminals emerged they were told by catholic priests go kill Serbs then come to church to be purified.
They were in it the whole way forcing locals into becoming "catholic" or face death. And the guy who was in charge of it all is now a saint in their book. 
"At that time fresh women and children arrived daily at the Camp in Stara Gradiška. About fourteen days later, Vrban [the Commandant of the Camp] ordered all children to be separated from their mothers and put into one room. Ten of us were told to carry them there in blankets. The children crawled about the room, and one child put an arm and leg through the doorway, so that the door could not be closed. Vrban shouted: 'Push it!' When I did not do that, he banged the door and crushed the child's leg. Then he took the child by its whole-leg, and banged it on the wall until it was dead. After that we continued carrying the children in. When the room was full, Vrban brought poison gas and killed them all."[127]
You have to be avare that this Jasenovac place is only one concentration camp. There were more and equally important they went from village to village town to town and killed people that is besides camps.

Millions of Serbs lost their lives and a lot of families lost their loved ones. Communists suppressed history for Yugoslavia's unity but since a lot of people
lost their family it was left in nations memory. In my country that is
common knowledge. Voices of those people never meant much to the world just
like Armenian voices. I dedicate this lame project of mine project to their memory hopping that their mention will be grain of salt in this not worthy
bundle of stuff.
DAMNED BE THE POPE latin heretic capitain!
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

plan is also to make ui extensible and to add rich text messaging via some rich text editor
architecture
-------------
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
+--------------------------------------------------------+
|                 buyan internal communication           |
|                                                        |
|    +----------+             +----------------------+   |
|    |  pubsub  |             |router message passing|   |
|    +----------+             +----------------------+   |
|                                                        |
+--------------------------------------------------------+

</pre>
component diagram
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
module dependencies
-------------
this is how modules are made
each module references few things
and router references all
and there is one module that makes the connection between router and individual modules
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
;this is how we call functions referenced in router
(c "x" arg1 argx ...) =
;and this is call flow internally from f we call fnc in pubsub it invokes router etc.
module1.f -> pubsub.send -> router.route -> module2.x -> return


</pre>

hooking into internals
-----------
this gives overview of how users will extend the buyan engine

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




this is async call it does not return something
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
and how too use it as a black box with message passing
you pass messages by triggering event on document

<br/>
pubsub messages are intercepted with setting
`window.messages.messagename`
<br/>
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
description of internal communication objects
each connection object has id of peer, writechannel and readchannel(core async channels) that can be used for reading and writing to peer
intercom meta collects channels ids for use latter on to broadcast messages for example or send
<pre>
conn
  peer id of peer
  writec writechannel ref
    readc readchannel ref
    conn connection ref
  readc readchannel ref
    conn connection reference
    writec writechannel ref

intercomMeta
  chanbyid
  p2pchans all channels of all peers
  knownPeers id of known peers
  knownPeersChannels writechannels of intercom peers
</pre>
<br/>
Buyan after
https://www.youtube.com/watch?v=4QtkDMXA0u8
with subtitles here https://www.youtube.com/watch?v=b3TQevs14CE
![](https://github.com/NikolaMandic/brandNameFrontendPrototype/blob/master/media/Ivanbilibin.jpg)

<br/>

Copyright (c) 2014 Nikola Mandic

recently twitter can be perceived as a truth unfriendly place they censored me 3 times as far as I noticed
because of tweets regarding latin heresy and various acts by nation stats coming from jesuit(devil's->since jesuits are godless heretics) doctrine of various fantasies justifying criminal activity
since jesuits and entire latin heretic cartel were/are criminals and they felt need possibly to word their behaviour in evil tricky way in order to shove it down people's throats
you can not have sex to be a virgin :-DD
but some people find it an attractive idea if jesuits tell them it can be done
