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
It picks video files from a watch folder and transcodes them into a new format based on a template. Transcoding does not happen with 
this software, but instead relies on a 3rd party software like handbrake. ffmpeg would probably work too.


# Features planned
* Watch folder for automatic encoding. All files will have a unified output format
* Drop zone for manual encoding. Manual encoding is just a means for when the custom output format is specified by yourself (like an override)
* Multiple Machines (Nodes) can encode. They will distribute the jobs so that no node encodes the same file as another one. A shared storage is needed
  for that feature (e.g. NFS, Gluster, CIFS, etc). Buy some Raspberry Pi's and happily transcode your library ;)
* Templates for settings
* A Docker image for easy install and setup.
* File-based configuration (at least for now). No database needed.
* Constraints for a single Node. Following constraints are planned:
  * Time-Window: E.g. Only transcode during the night
  * File size: If a video is very big, a Raspberry Pi might take a very long time. Leave the job for another server with more processing power.



# The Workflow
* 2 Input folders: Auto and Manual. Auto is for automatic encoding, the other is for manually provided video files. Manually created jobs are prioritized, but won't
  disrupt the current job.
* Each node picks a video file from the input folder, selects a template of options, and begins to transcode.
* The output file is being written either directly to your library or another location for manual quality verification or whatever you do with it. 
* Delete the source (if enabled and source is unneeded)
* Get the next video.

I'll provide some sort of flowchart sometime in the future. This project is not usable yet...

# FAQ

### What is "Manual Transcoding"
If you have a file that needs transcoding with different properties than the default template (e.g. dual audio track, multiple subtitles, ...) 
then you can provide a custom template which has all the options defined for that video file. Maybe you have a movie that you want to put on your mobile device
but it needs a different format than usual.

### What is "Automatic Transcoding"
If you have a software like Couchpotato which downloads movies and puts them in a output folder, you can pick them up and do some post processing after download.
If you are concerned about file size or unified codec use in your library, use this.

### Why not ffmpeg?
So far as I'm aware, ffmpeg does not support x265 which reduces file size in half but uses more CPU power. 
Encoding in x265 takes forever, but if I can do that unattended and automatically, I don't really care about time anymore.

### Why Java and not some script language like Bash?
I'm faster at coding in Java than bash. Simple as that.
