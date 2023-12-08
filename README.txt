# goal
It solves a personal problem while giving me the upportunity to write something fun:
  Encrypt/Decrypt text files in a session, without having to do much - for the layman!

  each time you write a text file, it's encrypted using a password String kept in the heap memory 


#the way it works:
when user registers (for the first time) he's asked for a password. after entering it:
- the passwd gets hashed (h0)
- salt's hashed (h1)
- h1+h0 is hashed (h2)

h2 gets saved in a file called hash.h2 or sth (you can check it under ${USER.HOME}/jurnall/${JURNALL_USER}/)

it will only be used to check user login. the advantage?
  if we save h0, the whole point of this program is gone to shit! bc anyone with physical access to user's computer
  can just use that to decrypt the EF out of all his files!

  so instead we save h2, and each time user logs in, he's asked for password. h2 get's generated and is checked with the 
  already saved h2

  but we don't use h2 for enc/dec

# enc/dec
now if we save h2 but don't use it to enc/dec, how is the program supposed to work?
  simple: we use h0 as a password (key) for enc/dec operations

what type of enc/dec algorithm you ask? it's AES. Big Bad Scary US Departments use it for cold storage, so why shouldn't we?
in fact when I searched for it online, I got the idea by visitting some .gov website!
