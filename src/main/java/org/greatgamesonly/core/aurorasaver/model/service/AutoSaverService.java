package org.greatgamesonly.core.aurorasaver.model.service;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.sun.jna.platform.win32.WinUser.*;

@Service
public class AutoSaverService {

    private Logger logger = LoggerFactory.getLogger(AutoSaverService.class);
    final User32 user32 = User32.INSTANCE;
    final Kernel32 kernel32 = Kernel32.INSTANCE;

    final Robot robot = new Robot();

    public AutoSaverService() throws AWTException {
    }

    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.MINUTES, initialDelay = 1)
    private void checkAndPerformAutoSave() {
        logger.info("AutoSaverService - checking if Aurora.exe is running");
        if(isProcessRunning("Aurora.exe")) {
            showAutoSavePopup();
        }
    }

    public void showAutoSavePopup() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                logger.info("AutoSaverService - BEGIN");
                logger.info("AutoSaverService - starting autosave dialogs");
                JFrame frame = new JFrame("AutoSave Notification");
                frame.setAlwaysOnTop(true);
                JOptionPane optionPane = new JOptionPane(
                        "Autosaving - Starting in 5 seconds...",
                        JOptionPane.INFORMATION_MESSAGE,
                        JOptionPane.DEFAULT_OPTION,
                        null, new Object[]{}, null);

                JDialog dialog = new JDialog(frame, "Notification", true);
                dialog.setContentPane(optionPane);

                // Set a timer to update the dialog and start the autosave in a separate thread
                Timer timer = new Timer(5000, e -> {
                    optionPane.setMessage("Autosaving - DO NOT TOUCH KEYBOARD OR MOUSE");
                    dialog.pack(); // Repack the dialog to adjust to the new message size

                    // Start autosave in a separate thread
                    new Thread(() -> {
                        logger.info("AutoSaverService - Performing autosave");
                        performAutoSave();
                        SwingUtilities.invokeLater(dialog::dispose); // Close dialog after autosave
                        logger.info("AutoSaverService - END");
                    }).start();
                });
                timer.setRepeats(false);
                timer.start();

                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });
    }

    private void performAutoSave() {
        try {
            logger.info("AutoSaverService - closing all sub windows");
            closeWindows(java.util.List.of(
                    "Naval Organization",
                    "Missile Design",
                    "Economics",
                    "Events",
                    "Medal Management",
                    "Commanders",
                    "Create Research Project",
                    "Class Design",
                    "Minerals",
                    "Ground Forces"
                    ),
                    "Aurora.exe"
            );
            logger.info("AutoSaverService - maximizing relevant window");
            maximizeFirstChildWindow("Aurora.exe");
            DimensionPlusPosition gameWindow = getProcessMainWindowDimensions("Aurora.exe");
            Point point = gameWindow.getRelativePositionInWindow(1132, 55);
            Thread.sleep(500);
            logger.info("AutoSaverService - taking control of mouse");
            robot.mouseMove(point.x, point.y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            logger.info("AutoSaverService - releasing control of mouse");
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public boolean isProcessRunning(String name) {
        AtomicBoolean result = new AtomicBoolean(false);
        ProcessHandle.allProcesses()
                .filter(processHandle -> processHandle.info().command().isPresent())
                .forEach(processHandle -> {
                    String processName = processHandle.info().command().get();
                    if (processName.contains(name)) {
                        result.set(true);
                    }
                });
        return result.get();
    }

    public void maximizeFirstChildWindow(String processName) {
        final boolean[] windowMaximized = {false};
        user32.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hWnd, Pointer userData) {
                if(!windowMaximized[0]) {
                    char[] windowText = new char[512];
                    user32.GetWindowText(hWnd, windowText, 512);
                    user32.GetWindowText(hWnd, windowText, 512);
                    String wText = Native.toString(windowText);

                    IntByReference pidRef = new IntByReference();
                    user32.GetWindowThreadProcessId(hWnd, pidRef);
                    user32.GetWindowThreadProcessId(hWnd, pidRef);
                    long processId = pidRef.getValue();
                    WinNT.HANDLE process = kernel32.OpenProcess(WinNT.PROCESS_QUERY_INFORMATION | WinNT.PROCESS_VM_READ, false, pidRef.getValue());
                    kernel32.CloseHandle(process);

                    if (!wText.isBlank() &&
                            !wText.equals(".NET-BroadcastEventWindow.4.0.0.0.141b42a.0") &&
                            !wText.equals("GDI+ Window (Aurora.exe)") &&
                            Objects.equals(getProcessPID(processName).get(0), processId)
                    ) {
                        user32.ShowWindow(hWnd, WS_MAXIMIZE);
                        try {
                            Thread.sleep(250);  // Wait a bit between commands
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        user32.ShowWindow(hWnd, WS_MINIMIZE);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        user32.ShowWindow(hWnd, SW_RESTORE);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        // Bring the window to the foreground
                        bringWindowToForeground(hWnd);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        user32.ShowWindow(hWnd, WS_MAXIMIZE);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        user32.ShowWindow(hWnd, WS_MINIMIZE);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        user32.ShowWindow(hWnd, SW_RESTORE);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        bringWindowToForeground(hWnd);
                        windowMaximized[0] = true;
                    }
                }
                return true;
            }
        }, null);
    }

    public DimensionPlusPosition getProcessMainWindowDimensions(String processName) {
        final DimensionPlusPosition[] result = {new DimensionPlusPosition(-1,-1, -1, -1, -1, -1)};
        final boolean[] valueRetrieved = {false};

        user32.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hWnd, Pointer userData) {
                if(!valueRetrieved[0]) {
                    char[] windowText = new char[512];
                    user32.GetWindowText(hWnd, windowText, 512);
                    user32.GetWindowText(hWnd, windowText, 512);
                    String wText = Native.toString(windowText);

                    IntByReference pidRef = new IntByReference();
                    user32.GetWindowThreadProcessId(hWnd, pidRef);
                    user32.GetWindowThreadProcessId(hWnd, pidRef);
                    long processId = pidRef.getValue();
                    WinNT.HANDLE process = kernel32.OpenProcess(WinNT.PROCESS_QUERY_INFORMATION | WinNT.PROCESS_VM_READ, false, pidRef.getValue());
                    kernel32.CloseHandle(process);

                    if (!wText.isBlank() &&
                            !wText.equals(".NET-BroadcastEventWindow.4.0.0.0.141b42a.0") &&
                            !wText.equals("GDI+ Window (Aurora.exe)") &&
                            Objects.equals(getProcessPID(processName).get(0), processId)
                    ) {
                        result[0] = getWindowSize(hWnd);
                        valueRetrieved[0] = true;
                    }
                }
                return true;
            }
        }, null);
        return result[0];
    }

    public DimensionPlusPosition getWindowSize(HWND hWnd) {
        final User32 user32 = User32.INSTANCE;
        RECT rect = new RECT();

        if (user32.GetWindowRect(hWnd, rect)) {
            int width = rect.right - rect.left;
            int height = rect.bottom - rect.top;
            return new DimensionPlusPosition(width, height, rect.top, rect.bottom, rect.left, rect.right);
        } else {
            return new DimensionPlusPosition(-1, -1, -1, -1, -1, -1); // Indicate an error
        }
    }

    public static class DimensionPlusPosition extends Dimension {
        public int top;
        public int bottom;
        public int left;

        public int right;

        public DimensionPlusPosition(int width, int height, int top, int bottom, int left, int right) {
            super(width,height);
            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
        }

        public Point getRelativePositionInWindow(int xLeftOffset, int yTopOffset) {
            int windowLeft = left;
            int windowTop = top;

            // Calculate the specific position within the window
            int specificX = windowLeft + xLeftOffset;
            int specificY = windowTop + yTopOffset; // Y coordinate remains the same if you only need horizontal offset

            return new Point(specificX, specificY);
        }

    }

    private void bringWindowToForeground(WinDef.HWND hWnd) {
        final User32 user32 = User32.INSTANCE;
        WinDef.HWND fgWindow = user32.GetForegroundWindow();
        if (!fgWindow.equals(hWnd)) {
            IntByReference fgThreadId = new IntByReference();
            IntByReference targetThreadId = new IntByReference();

            user32.GetWindowThreadProcessId(fgWindow, fgThreadId);
            user32.GetWindowThreadProcessId(hWnd, targetThreadId);

            user32.AttachThreadInput(new WinDef.DWORD(fgThreadId.getValue()), new WinDef.DWORD(targetThreadId.getValue()), true);
            user32.SetForegroundWindow(hWnd);
            user32.AttachThreadInput(new WinDef.DWORD(fgThreadId.getValue()), new WinDef.DWORD(targetThreadId.getValue()), false);
        }
    }

    private java.util.List<Long> getProcessPID(String processName) {
        AtomicReference<java.util.List<Long>> result = new AtomicReference<>(new ArrayList<>());
        ProcessHandle.allProcesses()
                .filter(ph -> ph.info().command().isPresent() && ph.info().command().get().contains(processName))
                .forEach(ph -> {
                    result.get().add(ph.pid());
                    ph.descendants().forEach(desc -> result.get().add(desc.pid()));
                });
        return result.get();
    }

    public void closeWindow(String fullOrPartialWindowTitle, String processName) {
        final User32 user32 = User32.INSTANCE;
        final Psapi psapi = Psapi.INSTANCE;
        final Kernel32 kernel32 = Kernel32.INSTANCE;

        user32.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hWnd, Pointer arg) {
                char[] windowText = new char[512];
                user32.GetWindowText(hWnd, windowText, 512);
                String wText = Native.toString(windowText);

                if (wText.contains(fullOrPartialWindowTitle)) {
                    IntByReference pidRef = new IntByReference();
                    user32.GetWindowThreadProcessId(hWnd, pidRef);
                    long processId = pidRef.getValue();
                    WinNT.HANDLE process = kernel32.OpenProcess(WinNT.PROCESS_QUERY_INFORMATION | WinNT.PROCESS_VM_READ, false, pidRef.getValue());

                    if (Objects.equals(getProcessPID(processName).get(0), processId)) {
                        user32.PostMessage(hWnd, WinUser.WM_CLOSE, null, null);
                    }

                    kernel32.CloseHandle(process);
                }

                return true;
            }
        }, null);
    }

    public void closeWindows(java.util.List<String> fullOrPartialWindowTitles, String processName) {
        final User32 user32 = User32.INSTANCE;
        final Psapi psapi = Psapi.INSTANCE;
        final Kernel32 kernel32 = Kernel32.INSTANCE;

        user32.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hWnd, Pointer arg) {
                char[] windowText = new char[512];
                user32.GetWindowText(hWnd, windowText, 512);
                String wText = Native.toString(windowText);

                if (fullOrPartialWindowTitles.stream().anyMatch(wText::contains)) {
                    IntByReference pidRef = new IntByReference();
                    user32.GetWindowThreadProcessId(hWnd, pidRef);
                    long processId = pidRef.getValue();
                    WinNT.HANDLE process = kernel32.OpenProcess(WinNT.PROCESS_QUERY_INFORMATION | WinNT.PROCESS_VM_READ, false, pidRef.getValue());

                    if (Objects.equals(getProcessPID(processName).get(0), processId)) {
                        user32.PostMessage(hWnd, WinUser.WM_CLOSE, null, null);
                    }

                    kernel32.CloseHandle(process);
                }

                return true;
            }
        }, null);
    }

}
