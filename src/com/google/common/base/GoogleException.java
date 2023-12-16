/*
 * Copyright (C) 2006 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.base;

import com.google.common.annotations.GoogleInternal;
import com.google.common.annotations.GwtIncompatible;
import javax.annotation.CheckForNull;
import org.jspecify.nullness.NullMarked;

/**
 * GoogleException allows you to associate two separate messages with an exception - an
 * internalMessage for storing debugging info, and an externalMessage suitable for displaying to a
 * customer.
 *
 * @author peter@google.com (Peter Kappler)
 * @deprecated Please use {@code Exception} (or a custom exception type) in new code.
 */
@Deprecated
@GoogleInternal
@GwtIncompatible
@NullMarked
public class GoogleException extends Exception {
  private static final long serialVersionUID = 1L;

  @CheckForNull private String internalMessage;
  @CheckForNull private String externalMessage = "A system error has occurred";

  public GoogleException() {}

  /**
   * Wraps a generic Java exception inside a GoogleException. The new GoogleException uses the stack
   * trace of the Java exception as its internal message.
   *
   * @param t the {@link Throwable} instance to wrap
   */
  public GoogleException(Throwable t) {
    super(t);
    setInternalMessage(Throwables.getStackTraceAsString(t));
  }

  /**
   * Wraps a generic Java exception inside a GoogleException. The new GoogleException uses the stack
   * trace of the Java exception as its internal message.
   *
   * @param t The exception to wrap
   * @param externalMessage the error message to show the end-user/customer
   */
  public GoogleException(Throwable t, @CheckForNull String externalMessage) {
    super(externalMessage, t);
    setInternalMessage(Throwables.getStackTraceAsString(t));
    setExternalMessage(externalMessage);
  }

  /**
   * Creates a new GoogleException with the specified internal error message. The external message
   * defaults to <i>A system error has occurred</i>.
   */
  public GoogleException(@CheckForNull String internalMessage) {
    super(internalMessage);
    setInternalMessage(internalMessage);
  }

  /**
   * Creates a new GoogleException with the specified internal and external error messages.
   *
   * @param internalMessage a message used for debugging
   * @param externalMessage a user-friendly message - usually displayed in a web page.
   */
  public GoogleException(
      @CheckForNull String internalMessage, @CheckForNull String externalMessage) {
    super(internalMessage);
    setInternalMessage(internalMessage);
    setExternalMessage(externalMessage);
  }

  /**
   * A low-level message that specifically describes the error. This should be a message that might
   * help a developer debug a problem. e.g. "Unable to open database connection. Check the DB
   * server!"
   */
  @CheckForNull
  public String getInternalMessage() {
    return internalMessage;
  }

  public final void setInternalMessage(@CheckForNull String s) {
    internalMessage = s;
  }

  /**
   * A user-friendly message that can be displayed in a web page to an end-user or customer. e.g.
   * "This service is temporarily off-line. Please try again in a few minutes."
   */
  @CheckForNull
  public String getExternalMessage() {
    return externalMessage;
  }

  public final void setExternalMessage(@CheckForNull String s) {
    externalMessage = s;
  }

  /** This implementation returns the same string as {@link #getInternalMessage}. */
  @CheckForNull
  @Override
  public String getMessage() {
    return getInternalMessage();
  }
}
