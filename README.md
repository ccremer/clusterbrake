# clusterbrake
A project which uses Handbrake to automatically transcode video files in a cluster.

So this is a little project which I'll use to automatically transcode my movies and tv shows into other formats and to save disk space.

## Who is it for
Well, 
* if you have some automatic downloaders like Couchpotato or Sickrage/Sickbeard,
* and you like to keep a nice and clean video library,
* you have some sort of server or NAS that can do things unattended
* you don't like the idea of on-the-fly transcoding of some other Home media player software

then this might be for you.

## What does it?
It picks video files from a watch folder and transcodes them into a new format based on a template. Transcoding does not happen with this software, but instead relies on a 3rd party software like handbrake or ffmpeg


# Features
* Watch folder for automatic encoding. All files will have a unified output format
* Multiple Machines (Nodes) can encode. They will distribute the jobs so that no node encodes the same file as another one. A shared storage is needed for that feature (e.g. NFS, GlusterFS, CIFS, etc). Buy some Raspberry Pi's and happily transcode your library ;)
* Templates for transcoder settings
* A Docker image for easy install and setup.
* File-based configuration (at least for now). No database needed.
* Constraints for a single Node. Following constraints are available:
  * Time-Window: E.g. Only transcode during the night
  * File size: If a video is very big, a Raspberry Pi might take a very long time. Leave the job for another server with more processing power.

# Project status
This project has been developed to work and is working in my personal environment, but it is currently under critical maintenance only (2016-09-15).
If you encounter a bug, open an issue, but I won't promise a fix.

# How exactly does it work?
Head over to the wiki and see the default workflow for yourself.
