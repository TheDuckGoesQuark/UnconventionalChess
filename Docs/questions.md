# Experiment and Questions

5 point Likert scale after playing, since we would ike to see similar results to that of (Computational models of emotion, personality, and social
relationships for interactions in games)

If possible, try get mixture of people who haven't played chess.

Our goal was to produce a video game, so it makes sense to take inspiration from the somewhat official "Game Engagement Questionnaire" (<https://reader.elsevier.com/reader/sd/pii/S0022103109000444?token=B02A1074151C48D2D0947C12D2BED37E853283ABA979E1E76F1D350E166ECB2E0B9532DFE04EAA7F54ADA4726E12B7C5>)

## GEQ Questions: 

1. I lose track of time
2. Things seem to happen automatically
3. I feel different
4. I feel scared
5. The game feels real
6. If someone talks to me, I don’t hear them
7. I get wound up
8. Time seems to kind of stand still or stop
9. I feel spaced out
10. I don’t answer when someone talks to me
11. I can’t tell that I’m getting tired
12. Playing seems automatic
13. My thoughts go fast
14. I lose track of where I am
15. I play without thinking about how to play
16. Playing makes me feel calm
17. I play longer than I meant to
18. I really get into the game
19. I feel like I just can’t stop playing

These used a -4 to 5 scoring system to measure absorption, flow, presence, and immersion

## Conversational Interface Questions 

Combining these with user experience with conversational interfaces as discussed <https://arxiv.org/pdf/1704.04579.pdf> here

* Linguistic accuracy of outputs
* Convincing, satisfying, & natural
  interaction
* Provide greetings, convey personality
* Give conversational cues
* Provide emotional information through
  tone, inflection, and expressivity 
* Make tasks more fun and interesting

A lot of human-agent conversation evaluations, but not much agent-agent conversation evaluations. One interesting metric that was pointed out by (Evaluating the conversation flow and content quality of a multi-bot conversational system) was 

> "Frequency and use of scroll bar by the users. We observed participants scrolling the text to contextualize themselves."

<https://www.cc.gatech.edu/arfacade/files/AEL-ARFacade-AAMAS07.pdf> did create an interactive conversation-centered drama, but again the player was more directly involved in the story and so the focus was again more on the human-computer interaction. 

<https://gaips.inesc-id.pt/component/gaips/publications/showPublicationPdf?pid=54&format=raw> also attempted to create a pervasive chess game, but they did not provide the questions used to evaulate their implementation so we could not compare results easily.

## Experiment

### Design 

Players will play two games, one of normal chess through the same user interface with a simple chess AI to act as an opponent.

Second playthrough will be against our agents, with dialogue turned on.

Order will vary to avoid this affecting results.

This will allow us to measure if ourimplementation provides more or less enjoyment, and see what factors it affects.

Questionnaires will be taken immediately after playthrough

### Questions

First three give us context about who is playing, from this we can gather if our implementations makes chess more enjoyable to those who already play it, or if it encourages those haven't to play:

1. I have played chess before (yes/no)

2. I enjoy playing chess (likert begins here)

3. I am good at chess.

   Question three tracks presence, do players find them selves 'hooked'

4. I lose track of time

   Question four measures absorption, are players emotinally affected due to the anthropormic pieces

5. I feel different

   Question five measures 'flow', do players feel like they are really playing against an army of other pieces.

6. The game feels real

   Again, flow, are players so involved in the game that they aren't distracted by outside influences.

7. If someone talks to me, I don’t hear them

   Does the game frustrate the player, are they invested in it (Flow)

8. I get wound up

   Is the player focused on the game (Presence)

9. My thoughts go fast

10. I lose track of where I am

    Comparison of people who have played chess and who haven't played here, does the social bots distract from the actual game

11. I play without thinking about how to play

    If not an intense experience, do the players enjoy their time (Flow)

12. Playing makes me feel calm

    (Presence)

13. I play longer than I meant to

    (Immersion)

14. I really get into the game

    (Flow)

15. I feel like I just can’t stop playing

    The next set of questions are more regarding the natural language aspect, they won't be asked for the non-agent playthrough

16. The pieces spoke with linguistic accuracy of outputs? (1-7)

17. The pieces had convincing, satisfying, & natural interactions

18. The pieces convey personality

19. The pieces provide emotional information through
    tone, inflection, and expressivity 

20. The pieces made the game more fun and interesting


From answer we can determine if the social pieces increase presence, immersion, flow, and absorption, and compare it to normal chess. Our  hypothesis is that the social robots improve this compared to regular chess.

^^ This is too much ethics wise, but we can do something like this:

## Revised Experiment

15 minutes per volunteer. 

Prepare: three situations, record on video for participants to watch. 2 minutes each?

Allow: participant to play a game from scratch too. 4 minutes.

5 minutes for questionnaire at the end, with probably ten questions from the above, and one free comment question.