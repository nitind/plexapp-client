/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.core.externalplayer;

import android.content.Intent;

/**
 * Represents the capabilities of an external player.
 *
 * @author dcarver
 */
public interface ExternalPlayer {

  public void launch();

  /**
   * Whether the external player supports resume offset position for starting
   * playback.
   */
  public boolean supportsResume();

  /**
   * Whether the external player supports reporting that it will report it's
   * last playback position or not. Not all players will pass this information
   * back as an activity result. If a player doesn't support this feature then
   * the Plex playback position can't be updated.
   */
  public boolean supportsPlaybackPosition();

  /**
   * Whether subtitles can be passed into the player for playback.
   */
  public boolean supportsSubtitleUrls();

  /**
   * Whether the title of the video can be passed to the external player or
   * not.
   */

  public boolean hasTitleSupport();

  public boolean hasHardwareDecodingSupport();

  public void enableHardwareDecodinging();

  public void disableHadwareDecoding();

  public Intent createIntent();
}
