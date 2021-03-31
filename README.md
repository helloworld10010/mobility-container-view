# mobility-container-view

##### <img src="screens\screen1.gif" style="zoom:50%;" />

#### Usage

```xml
<com.helloworld.mobility_view.MobilityContainerView
        android:layout_width="150dp"
        android:layout_height="150dp"
        android:background="#fda"
        >
		<!--想移动的控件放它下边-->
        <Button
            android:id="@+id/btn"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Hello!"
            android:onClick="onClick"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            />
    </com.helloworld.mobility_view.MobilityContainerView>
```

