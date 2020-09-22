## Color Scheme
  - Go to: `File > Settings > Editor > FileTypes`
  - Add new item to the `Recognized File Type` list
    - Name: `Jamplate`
    - Description: `Jamplate`
    - Line Comment: `\\`
    - Only At Line Start: `unchecked`
    - Block comment start: `/*`
    - Block comment end: `*/`
    - Hex prefix: `0x`
    - Number postfixes: `D, F, L`
    - Support paired braces: `checked`
    - Support paired brackets: `checked`
    - Support paired parens: `checked`
    - Support string escapes: `unchecked`
    - Keywords (1)
        ```
        ,
        ;
        abstract
        assert
        boolean
        break
        byte
        case
        catch
        char
        class
        const
        continue
        default
        do
        double
        else
        enume
        exports
        extends
        false
        final
        finally
        float
        for
        goto
        if
        implements
        import
        instanceof
        int
        interface
        long
        module
        native
        new
        null
        package
        private
        protected
        public
        requires
        return
        short
        static
        strictfp
        super
        switch
        synchronized
        this
        throw
        throws
        transient
        true
        try
        var
        void
        volatile
        while
        ```
    - Keywords (2):
        ```
        ```
    - Keywords (3):
        ```
        0
        1
        2
        3
        4
        5
        6
        7
        8
        9
        \#
        \,
        \|
        ```
    - Keywords (4):
        ```
        #
        #DEFINE
        #Define
        #ELIF
        #ELSE
        #ENDIF
        #ENDWITH
        #ElIf
        #Elif
        #Else
        #EndIf
        #EndWith
        #Endif
        #Endwith
        #IF
        #If
        #LINE
        #LN
        #Line
        #Ln
        #MAKE
        #Make
        #PASTE
        #Paste
        #TEXT
        #Text
        #VAR
        #Var
        #WITH
        #With
        #define
        #elif
        #else
        #endif
        #endwith
        #if
        #line
        #ln
        #make
        #paste
        #text
        #var
        #with
        ```
    - Ignore case: `unchecked`
    - Click `OK`

  - Add a new item to the `File Name Pattern` list after choosing the previously created `Jamplate` File Type.
    - Type `*.jamplate`
    - Click `OK`

  - Now, intellij has syntax highlighting for `.jamplate` files!
